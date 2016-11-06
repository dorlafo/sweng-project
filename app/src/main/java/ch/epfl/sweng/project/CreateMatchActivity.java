package ch.epfl.sweng.project;


import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ch.epfl.sweng.project.error.ErrorHandlerUtils;
import ch.epfl.sweng.project.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.project.model.GPSPoint;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Match.GameVariant;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.tools.LocationProvider;
import ch.epfl.sweng.project.tools.TimePickerFragment;

public class CreateMatchActivity extends BaseActivity implements
        OnClickListener,
        OnItemSelectedListener,
        OnTimeSetListener {

    private static final String TAG = CreateMatchActivity.class.getSimpleName();

    Button createMatchButton;

    private Match.Builder matchBuilder;
    private LocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);
        matchBuilder = new Match.Builder();

        // TODO: make user choose location
        locationProvider = new LocationProvider(this);
        Location currentLocation = locationProvider.getLastLocation();
        if (currentLocation != null) {
            matchBuilder.setLocation(new GPSPoint(currentLocation.getLatitude(),
                    currentLocation.getLongitude()));
        }

        createMatchButton = (Button) findViewById(R.id.create_create_button);
        createMatchButton.setEnabled(false);
        createMatchButton.setOnClickListener(this);

        final EditText editText = (EditText) findViewById(R.id.description_match_text);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String description = v.getText().toString();
                    if (description.length() != 0) {
                        matchBuilder.setDescription(description);
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String description = ((TextView) v).getText().toString();
                    if (description.length() != 0) {
                        matchBuilder.setDescription(description);
                    }
                }
            }
        });

        ImageButton timePickerDialog = (ImageButton) findViewById(R.id.time_picker_button);
        timePickerDialog.setOnClickListener(this);

        Calendar defaultExpirationDate = Calendar.getInstance();
        defaultExpirationDate.add(Calendar.HOUR_OF_DAY, 2);
        displayCurrentExpirationDate(defaultExpirationDate);

        Button addPlayer = (Button) findViewById(R.id.add_player_button);
        addPlayer.setOnClickListener(this);

        Switch privacySwitch = (Switch) findViewById(R.id.switch_private);
        privacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                matchBuilder.setPrivacy(isChecked);
            }
        });

        Spinner variantSpinner = (Spinner) findViewById(R.id.variant_spinner);

        ArrayAdapter<GameVariant> variantAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, GameVariant.values());
        variantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        variantSpinner.setAdapter(variantAdapter);
        variantSpinner.setOnItemSelectedListener(this);

        addCurrentUserToBuilder();
    }

    @Override
    public void onResume() {
        super.onResume();
        locationProvider.connectGoogleApiClient();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationProvider.stopLocationUpdates();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_create_button:
                // TODO: retrieve gps position
                DBReferenceWrapper ref = dbRefWrapped.child("matches");
                String matchId = ref.push().getKey();
                ref.child(matchId).setValue(matchBuilder.setMatchID(matchId).build());
                Log.d(TAG, "Pushed match " + matchId + " to database");
                Intent moveToMatchActivity = new Intent(this, MatchActivity.class);
                getIntent().putExtra("MATCH_ID", matchId);
                startActivity(moveToMatchActivity);
                break;
            case R.id.time_picker_button:
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.add_player_button:
                // TODO: add player with player provider
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
        switch (parent.getId()) {
            case R.id.variant_spinner:
                matchBuilder.setVariant((GameVariant) item);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar expirationTime = Calendar.getInstance();
        expirationTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        expirationTime.set(Calendar.MINUTE, minute);

        final Calendar currentTime = Calendar.getInstance();

        if (expirationTime.compareTo(currentTime) > 0) {
            displayCurrentExpirationDate(expirationTime);
            matchBuilder.setExpirationTime(expirationTime.getTimeInMillis());
        }
        // TODO: warning or error for time before current time
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addCurrentUserToBuilder() {
        String currentUserId = fAuth.getCurrentUser().getDisplayName();
        dbRefWrapped.child("players").child(currentUserId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            matchBuilder.addPlayer(dataSnapshot.getValue(Player.class));
                        } catch(IllegalStateException e) {
                            ErrorHandlerUtils.sendErrorMessage(CreateMatchActivity.this, R.string.match_is_full, "Sorry, desired match is full");
                        } catch(IllegalAccessException a) {
                            ErrorHandlerUtils.sendErrorMessage(CreateMatchActivity.this, R.string.cannot_join_match, "You are already signed into that Match");
                        }
                        createMatchButton.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void displayCurrentExpirationDate(Calendar calendar) {
        TextView currentExpirationDate =
                (TextView) findViewById(R.id.current_expiration_time);
        DateFormat dateFormat = new SimpleDateFormat(
                getString(R.string.create_date_format), Locale.FRENCH);
        currentExpirationDate.setText(dateFormat.format(calendar.getTimeInMillis()));
    }

}
