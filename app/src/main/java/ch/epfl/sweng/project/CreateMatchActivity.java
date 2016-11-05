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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
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

/**
 * Activity used to create a match.
 * <br>
 * The current user is automatically added to the match.
 * The creation options are: inputting a short description of the match,
 * manually adding a player, choosing the match location, if it will be
 * private or not, its variant, and its expiration date.
 */
public class CreateMatchActivity extends AppCompatActivity implements
        OnClickListener,
        OnItemSelectedListener,
        OnTimeSetListener,
        OnDateSetListener {

    private static final String TAG = CreateMatchActivity.class.getSimpleName();
    private final int PLACE_PICKER_REQUEST = 27;

    private Button createMatchButton;
    private ImageButton placePickerButton;

    private Match.Builder matchBuilder;
    private LocationProvider locationProvider;
    private Calendar matchCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);
        matchBuilder = new Match.Builder();
        locationProvider = new LocationProvider(this);

        createMatchButton = (Button) findViewById(R.id.create_create_button);
        createMatchButton.setEnabled(false);
        createMatchButton.setOnClickListener(this);

        // Description input
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

        // Date and time pickers
        ImageButton timePickerDialog = (ImageButton) findViewById(R.id.time_picker_button);
        timePickerDialog.setOnClickListener(this);

        ImageButton datePickerDialog = (ImageButton) findViewById(R.id.date_picker_button);
        datePickerDialog.setOnClickListener(this);

        matchCalendar = Calendar.getInstance();
        matchCalendar.add(Calendar.HOUR_OF_DAY, 2);
        displayCurrentExpirationDate();

        // Add player
        Button addPlayer = (Button) findViewById(R.id.add_player_button);
        addPlayer.setOnClickListener(this);

        // Private match
        Switch privacySwitch = (Switch) findViewById(R.id.switch_private);
        privacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                matchBuilder.setPrivacy(isChecked);
            }
        });

        // Variant
        ArrayAdapter<GameVariant> variantAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, GameVariant.values());
        variantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner variantSpinner = (Spinner) findViewById(R.id.variant_spinner);
        variantSpinner.setAdapter(variantAdapter);
        variantSpinner.setOnItemSelectedListener(this);

        // Place picker
        placePickerButton = (ImageButton) findViewById(R.id.create_place_picker_button);
        placePickerButton.setEnabled(false);
        placePickerButton.setOnClickListener(this);

        if (locationProvider.locationPermissionIsGranted()) {
            Location currentLocation = locationProvider.getLastLocation();
            if (currentLocation != null) {
                matchBuilder.setLocation(new GPSPoint(currentLocation.getLatitude(),
                        currentLocation.getLongitude()));
            }
            placePickerButton.setEnabled(true);
        }

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
            case R.id.create_place_picker_button:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                    placePickerButton.setEnabled(false);
                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(this, R.string.error_play_services_not_available, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            LatLng location = place.getLatLng();
            matchBuilder.setLocation(new GPSPoint(location.latitude, location.longitude));
            placePickerButton.setEnabled(true);
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
    public void onNothingSelected(AdapterView<?> parent) {

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
            Toast invalidHourToast = Toast.makeText(this,
                    R.string.create_toast_invalid_hour, Toast.LENGTH_SHORT);
            invalidHourToast.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar currentTime = Calendar.getInstance();
        int currentYear = currentTime.get(Calendar.YEAR);
        int currentMonth = currentTime.get(Calendar.MONTH);
        int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);

        if (year == currentYear && month == currentMonth && dayOfMonth == currentDay) {
            matchCalendar.set(year, month, dayOfMonth);

            int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
            int currentMinute = currentTime.get(Calendar.MINUTE);

            if (matchCalendar.get(Calendar.HOUR_OF_DAY) < currentHour
                    || (matchCalendar.get(Calendar.HOUR_OF_DAY) == currentHour
                    && matchCalendar.get(Calendar.MINUTE) < currentMinute)) {
                matchCalendar.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY));
                matchCalendar.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE));
            }
            matchBuilder.setExpirationTime(matchCalendar.getTimeInMillis());
            displayCurrentExpirationDate();
        } else {
            Calendar tempCalendar = (Calendar) matchCalendar.clone();
            tempCalendar.set(year, month, dayOfMonth);

            if (tempCalendar.compareTo(currentTime) > 0) {
                matchCalendar.set(year, month, dayOfMonth);
                matchBuilder.setExpirationTime(matchCalendar.getTimeInMillis());
                displayCurrentExpirationDate();
            } else {
                Toast invalidDateToast = Toast.makeText(this,
                        R.string.create_toast_invalid_date, Toast.LENGTH_SHORT);
                invalidDateToast.show();
            }
        }
    }

    private void addCurrentUserToBuilder() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        FirebaseDatabase.getInstance().getReference().child("players").child(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
        TextView currentExpirationDate = (TextView) findViewById(R.id.current_expiration_time);
        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.create_date_format), Locale.FRENCH);
        currentExpirationDate.setText(dateFormat.format(matchCalendar.getTimeInMillis()));
    }

}
