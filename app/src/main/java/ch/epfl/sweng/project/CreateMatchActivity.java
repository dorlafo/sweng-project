package ch.epfl.sweng.project;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import ch.epfl.sweng.project.database.MatchDatabaseInterface;
import ch.epfl.sweng.project.database.UserProvider;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Match.GameVariant;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.tools.TimePickerFragment;

public class CreateMatchActivity extends AppCompatActivity implements
        OnClickListener,
        OnItemSelectedListener,
        OnTimeSetListener {

    //MatchDatabaseInterface matchDBInterface;
    //UserProvider userProvider;
    Button createMatchButton;

    private Match.Builder matchBuilder;
    private static final String TAG = CreateMatchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //userProvider = new UserProvider();
        //userProvider.addEventListener(new ButtonEnabler());
        //matchDBInterface = new MatchDatabaseInterface();
        matchBuilder = new Match.Builder();

        setContentView(R.layout.activity_create_match);
        // TODO: set match location using GPS or maps view

        createMatchButton = (Button) findViewById(R.id.create_create_button);
        createMatchButton.setEnabled(false);
        createMatchButton.setOnClickListener(this);

        final EditText editText = (EditText) findViewById(R.id.description_match_text);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    matchBuilder.setDescription(v.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        ImageButton timePickerDialog = (ImageButton) findViewById(R.id.time_picker_button);
        timePickerDialog.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_create_button:
                //Player currentPlayer = userProvider.getPlayerWithID(currentUserId);
                //Player currentPlayer =
                // TODO: retrieve gps position
                //matchBuilder.setLocation(findPosition());
                //matchBuilder.addPlayer(currentPlayer);
                //String matchId = matchDBInterface.writeNewMatch(matchBuilder.build());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("matches");
                String matchId = ref.push().getKey();
                ref.child(matchId).setValue(matchBuilder.build());
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
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar expirationTime = Calendar.getInstance();
        expirationTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        expirationTime.set(Calendar.MINUTE, minute);

        final Calendar currentTime = Calendar.getInstance();

        if (expirationTime.compareTo(currentTime) > 0) {
            matchBuilder.setExpirationTime(expirationTime.getTimeInMillis());
        }
        // TODO: warning or error for time before current time
    }

    /*
    private class ButtonEnabler implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.getKey().equals(currentUserId)) {
                createMatchButton.setEnabled(true);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(DatabaseError databaseError) { }
    }
    */
}
