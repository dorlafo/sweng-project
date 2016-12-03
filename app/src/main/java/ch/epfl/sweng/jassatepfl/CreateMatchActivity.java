package ch.epfl.sweng.jassatepfl;


import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import android.widget.ListView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ch.epfl.sweng.jassatepfl.error.ErrorHandlerUtils;
import ch.epfl.sweng.jassatepfl.model.GPSPoint;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Match.GameVariant;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.notification.InvitePlayer;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import ch.epfl.sweng.jassatepfl.tools.DatePickerFragment;
import ch.epfl.sweng.jassatepfl.tools.LocationProvider;
import ch.epfl.sweng.jassatepfl.tools.TimePickerFragment;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Activity used to create a match.
 * <br>
 * The current user is automatically added to the match.
 * The creation options are: inputting a short description of the match,
 * manually adding a player, choosing the match location, if it will be
 * private or not, its variant, and its expiration date.
 *
 * @author Nicolas Phan Van
 */
public class CreateMatchActivity extends BaseActivityWithNavDrawer implements
        OnClickListener,
        OnItemSelectedListener,
        OnTimeSetListener,
        OnDateSetListener {

    private static final String TAG = CreateMatchActivity.class.getSimpleName();
    private static final int PLACE_PICKER_REQUEST = 27;
    private static final int ADD_PLAYER_REQUEST = 0;

    private Button createMatchButton;
    private ImageButton placePickerButton;
    private Match.Builder matchBuilder;
    private LocationProvider locationProvider;
    private ArrayAdapter<Player> playerArrayAdapter;
    private Calendar matchCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fAuth.getCurrentUser() == null) {
            //Log.d(TAG, "showLogin:getCurrentUser:null");
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            //Log.d(TAG, "showLogin:getCurrentUser:NOTnull");
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_create_match, drawer, false);
            drawer.addView(contentView, 0);

            matchBuilder = new Match.Builder();
            locationProvider = new LocationProvider(this, false);

            createMatchButton = (Button) findViewById(R.id.create_create_button);
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

            // Date and time pickers
            ImageButton timePickerDialog = (ImageButton) findViewById(R.id.time_picker_button);
            timePickerDialog.setOnClickListener(this);

            ImageButton datePickerDialog = (ImageButton) findViewById(R.id.date_picker_button);
            datePickerDialog.setOnClickListener(this);

            matchCalendar = Calendar.getInstance();
            matchCalendar.add(HOUR_OF_DAY, 1);
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

            // Player list
            TextView emptyList = new TextView(this);
            emptyList.setText(R.string.create_empty_list);
            emptyList.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            emptyList.setTextColor(Color.GRAY);

            //TODO: Doesn't show players... --> fix this + make clicking on player ask to remove him
            ListView playersLV = (ListView) findViewById(R.id.create_player_list);
            ((ViewGroup) playersLV.getParent()).addView(emptyList);
            playersLV.setEmptyView(emptyList);
            playersLV.setBackgroundColor(0xFAFAFA);

            playerArrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, new ArrayList<Player>());
            playersLV.setAdapter(playerArrayAdapter);

            playersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Player player = playerArrayAdapter.getItem(position);
                    new AlertDialog.Builder(CreateMatchActivity.this)
                            .setTitle(R.string.dialog_remove_player)
                            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    playerArrayAdapter.remove(player);
                                }
                            })
                            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing, goes back to InvitePlayerToMatchActivity
                                }
                            })
                            .show();
                }
            });

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
            }

            addCurrentUserToBuilder();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        locationProvider.connectGoogleApiClient();
        if (locationProvider.locationPermissionIsGranted()) {
            placePickerButton.setEnabled(true);
        }
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
                if (matchBuilder.getPlayerList().size() == 0) {
                    Toast.makeText(CreateMatchActivity.this,
                            R.string.toast_cannot_create_with_no_player, Toast.LENGTH_SHORT)
                            .show();
                    createMatchButton.setEnabled(false);
                } else {
                    String matchId = dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES).push().getKey();
                    Match m = matchBuilder.setMatchID(matchId).build();
                    dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES).child(matchId).setValue(m);
                    dbRefWrapped.child(DatabaseUtils.DATABASE_PENDING_MATCHES).child(matchId).child(getUserSciper()).setValue(false);
                    //Log.d(TAG, "Pushed match " + matchId + " to database");
                    new InvitePlayer(playerArrayAdapter).execute(matchId);
                    startActivity(new Intent(this, WaitingPlayersActivity.class).putExtra("match_Id", matchId));
                    finish();
                }
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
                startActivityForResult(new Intent(this, InvitePlayerToMatchActivity.class), ADD_PLAYER_REQUEST);
                break;
            case R.id.create_place_picker_button:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                    placePickerButton.setEnabled(false);
                } catch (GooglePlayServicesRepairableException e) {
                    //noinspection deprecation
                    GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(this, R.string.error_play_services_not_available, Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add all players sent by InvitePlayerToMatchActivity
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_PLAYER_REQUEST:
                    int playerNum = data.getIntExtra("players_added", 0);
                    for (int i = 0; i < playerNum; i++) {
                        String sciper = data.getStringExtra("player" + i);
                        dbRefWrapped.child(DatabaseUtils.DATABASE_PLAYERS)
                                .child(sciper)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Player player = dataSnapshot.getValue(Player.class);
                                        if (playerArrayAdapter.getPosition(player) == -1) {
                                            playerArrayAdapter.add(player);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("ERROR-DATABASE", databaseError.toString());
                                    }
                                });
                    }
                    break;
                case PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(this, data);
                    LatLng location = place.getLatLng();
                    matchBuilder.setLocation(new GPSPoint(location.latitude, location.longitude));
                    placePickerButton.setEnabled(true);
                    break;
                default:
                    break;
            }
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
        // Necessary for implementation of OnItemSelected interface
        // Never used, do nothing.
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar tempCalendar = (Calendar) matchCalendar.clone();
        tempCalendar.set(HOUR_OF_DAY, hourOfDay);
        tempCalendar.set(MINUTE, minute);

        final Calendar currentTime = Calendar.getInstance();
        System.out.println("currentTime:" + currentTime);
        System.out.println("matchCalendar:" + matchCalendar);
        if (tempCalendar.compareTo(currentTime) > 0) {
            matchCalendar.setTimeInMillis(tempCalendar.getTimeInMillis());
            matchBuilder.setExpirationTime(matchCalendar.getTimeInMillis());
            displayCurrentExpirationDate();
        } else {
            Toast.makeText(this, R.string.toast_invalid_hour, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar currentTime = Calendar.getInstance();
        int currentYear = currentTime.get(YEAR);
        int currentMonth = currentTime.get(MONTH);
        int currentDay = currentTime.get(DAY_OF_MONTH);

        if (year == currentYear && month == currentMonth && dayOfMonth == currentDay) {
            matchCalendar.set(year, month, dayOfMonth);

            int currentHour = currentTime.get(HOUR_OF_DAY);
            int currentMinute = currentTime.get(MINUTE);

            if (matchCalendar.get(HOUR_OF_DAY) < currentHour
                    || (matchCalendar.get(HOUR_OF_DAY) == currentHour
                    && matchCalendar.get(MINUTE) < currentMinute)) {
                matchCalendar.set(HOUR_OF_DAY, currentHour);
                matchCalendar.set(MINUTE, currentMinute);
            }
            matchBuilder.setExpirationTime(matchCalendar.getTimeInMillis());
            displayCurrentExpirationDate();
        } else {
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.set(year, month, dayOfMonth);

            if (tempCalendar.compareTo(currentTime) > 0) {
                matchCalendar.set(year, month, dayOfMonth);
                matchBuilder.setExpirationTime(matchCalendar.getTimeInMillis());
                displayCurrentExpirationDate();
            } else {
                Toast.makeText(this, R.string.toast_invalid_date, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void addCurrentUserToBuilder() {
        // TODO: can we fuse this method with addplayer l.258, it is almost the same
        try {
            String currentUserId = fAuth.getCurrentUser().getDisplayName();
            dbRefWrapped.child(DatabaseUtils.DATABASE_PLAYERS).child(currentUserId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                Player currentUser = dataSnapshot.getValue(Player.class);
                                matchBuilder.addPlayer(currentUser);
                                createMatchButton.setEnabled(true);
                            } catch (IllegalStateException e) {
                                ErrorHandlerUtils.sendErrorMessage(CreateMatchActivity.this,
                                        R.string.error_cannot_join, R.string.error_match_full);
                            } catch (IllegalAccessException a) {
                                ErrorHandlerUtils.sendErrorMessage(CreateMatchActivity.this,
                                        R.string.error_cannot_join, R.string.error_already_in_match);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (NullPointerException e) {
            Toast.makeText(this, R.string.toast_no_connection, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Helper method to display expiration date as string.
     */
    private void displayCurrentExpirationDate() {
        TextView currentExpirationDate = (TextView) findViewById(R.id.current_expiration_time);
        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format_match_creation), Locale.FRENCH);
        currentExpirationDate.setText(dateFormat.format(matchCalendar.getTimeInMillis()));
    }

}
