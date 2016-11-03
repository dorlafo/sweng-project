package ch.epfl.sweng.project;


import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ch.epfl.sweng.project.model.GPSPoint;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Match.GameVariant;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.tools.DatePickerFragment;
import ch.epfl.sweng.project.tools.LocationProvider;
import ch.epfl.sweng.project.tools.TimePickerFragment;

public class CreateMatchActivity extends AppCompatActivity implements
        OnClickListener,
        OnItemSelectedListener,
        OnTimeSetListener,
        OnDateSetListener {

    private static final String TAG = CreateMatchActivity.class.getSimpleName();

    private Button createMatchButton;

    private Match.Builder matchBuilder;
    private LocationProvider locationProvider;
    private Calendar matchCalendar;

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

        matchCalendar = Calendar.getInstance();
        matchCalendar.add(Calendar.HOUR_OF_DAY, 2);
        displayCurrentExpirationDate();

        ImageButton datePickerDialog = (ImageButton) findViewById(R.id.date_picker_button);
        datePickerDialog.setOnClickListener(this);

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
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("matches");
                String matchId = ref.push().getKey();
                ref.child(matchId).setValue(matchBuilder.setMatchID(matchId).build());
                Log.d(TAG, "Pushed match " + matchId + " to database");
                Intent moveToMatchActivity = new Intent(this, MatchActivity.class);
                getIntent().putExtra("MATCH_ID", matchId);
                startActivity(moveToMatchActivity);
                break;
            case R.id.time_picker_button:
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.date_picker_button:
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
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
        Calendar tempCalendar = (Calendar) matchCalendar.clone();
        tempCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        tempCalendar.set(Calendar.MINUTE, minute);

        final Calendar currentTime = Calendar.getInstance();

        if (tempCalendar.compareTo(currentTime) > 0) {
            matchCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            matchCalendar.set(Calendar.MINUTE, minute);
            matchBuilder.setExpirationTime(matchCalendar.getTimeInMillis());
            displayCurrentExpirationDate();
        } else {
            Toast invalidTimeToast = Toast.makeText(this, R.string.create_toast_invalid_time, Toast.LENGTH_SHORT);
            invalidTimeToast.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar tempCalendar = (Calendar) matchCalendar.clone();
        tempCalendar.set(Calendar.YEAR, year);
        tempCalendar.set(Calendar.MONTH, month);
        tempCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        final Calendar currentTime = Calendar.getInstance();

        if (tempCalendar.compareTo(currentTime) > 0) {
            matchCalendar.set(Calendar.YEAR, year);
            matchCalendar.set(Calendar.MONTH, month);
            matchCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            matchBuilder.setExpirationTime(matchCalendar.getTimeInMillis());
            displayCurrentExpirationDate();
        } else {
            Toast invalidTimeToast = Toast.makeText(this, R.string.create_toast_invalid_time, Toast.LENGTH_SHORT);
            invalidTimeToast.show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addCurrentUserToBuilder() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        FirebaseDatabase.getInstance().getReference().child("players").child(currentUserId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        matchBuilder.addPlayer(dataSnapshot.getValue(Player.class));
                        createMatchButton.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void displayCurrentExpirationDate() {
        TextView currentExpirationDate =
                (TextView) findViewById(R.id.current_expiration_time);
        DateFormat dateFormat = new SimpleDateFormat(
                getString(R.string.create_date_format), Locale.FRENCH);
        currentExpirationDate.setText(dateFormat.format(matchCalendar.getTimeInMillis()));
    }
}
