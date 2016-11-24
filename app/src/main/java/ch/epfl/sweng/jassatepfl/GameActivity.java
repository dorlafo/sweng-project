package ch.epfl.sweng.jassatepfl;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Match.Meld;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;

import static ch.epfl.sweng.jassatepfl.GameActivity.Caller.FIRST_TEAM;
import static ch.epfl.sweng.jassatepfl.GameActivity.Caller.SECOND_TEAM;

public class GameActivity extends BaseAppCompatActivity implements
        OnClickListener, OnItemSelectedListener {

    private final static int TOTAL_POINTS_IN_ROUND = 157;
    private final static int MATCH_POINTS = 257;

    private Match currentMatch;
    private MatchStats matchStats;

    private TextView firstTeamScoreDisplay;
    private TextView secondTeamScoreDisplay;

    private int firstTeamScore;
    private int secondTeamScore;

    protected enum Caller {FIRST_TEAM, SECOND_TEAM}

    private Caller caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Intent intent = getIntent();
        //String matchId = intent.getStringExtra("match_Id");
        String matchId = "-KXIHjko1keaaJX8iqhy";
        dbRefWrapped.child("matches").child(matchId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentMatch = dataSnapshot.getValue(Match.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        firstTeamScoreDisplay = (TextView) findViewById(R.id.score_display_1);
        secondTeamScoreDisplay = (TextView) findViewById(R.id.score_display_2);
        firstTeamScore = 0;
        secondTeamScore = 0;

        ImageButton firstTeamUpdateButton = (ImageButton) findViewById(R.id.score_update_1);
        firstTeamUpdateButton.setOnClickListener(this);
        ImageButton secondTeamUpdateButton = (ImageButton) findViewById(R.id.score_update_2);
        secondTeamUpdateButton.setOnClickListener(this);

        ArrayAdapter<Meld> meldAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Meld.values());
        meldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner firstMeldSpinner = (Spinner) findViewById(R.id.score_meld_spinner_1);
        firstMeldSpinner.setAdapter(meldAdapter);
        firstMeldSpinner.setOnItemSelectedListener(this);

        Spinner secondMeldSpinner = (Spinner) findViewById(R.id.score_meld_spinner_2);
        secondMeldSpinner.setAdapter(meldAdapter);
        secondMeldSpinner.setOnItemSelectedListener(this);

        //matchStats = new MatchStats(matchId, currentMatch.getGameVariant(), teamList); TODO: ce truc
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score_update_1:
                caller = FIRST_TEAM;
                break;
            case R.id.score_update_2:
                caller = SECOND_TEAM;
                break;
        }
        showScorePicker();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Meld meld = (Meld) parent.getItemAtPosition(position);
        int meldValue = meld.value();
        switch (parent.getId()) {
            case R.id.score_meld_spinner_1:
                updateScore(meldValue, 0);
                break;
            case R.id.score_meld_spinner_2:
                updateScore(0, meldValue);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void computeScores(int callerScore) {
        int otherTeamScore = TOTAL_POINTS_IN_ROUND - callerScore;
        switch (caller) {
            case FIRST_TEAM:
                updateScore(callerScore, otherTeamScore);
                break;
            case SECOND_TEAM:
                updateScore(otherTeamScore, callerScore);
                break;
        }
    }

    private void updateScore(int firstTeamScore, int secondTeamScore) {
        this.firstTeamScore += firstTeamScore;
        firstTeamScoreDisplay.setText(Integer.toString(this.firstTeamScore));

        this.secondTeamScore += secondTeamScore;
        secondTeamScoreDisplay.setText(Integer.toString(this.secondTeamScore));
    }

    private void showScorePicker() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("hello");
        dialog.setContentView(R.layout.score_picker);
        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.score_picker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(TOTAL_POINTS_IN_ROUND);
        numberPicker.setWrapSelectorWheel(false);

        Button match = (Button) dialog.findViewById(R.id.score_picker_match);
        match.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (caller) {
                    case FIRST_TEAM:
                        updateScore(MATCH_POINTS, 0);
                        break;
                    case SECOND_TEAM:
                        updateScore(0, MATCH_POINTS);
                        break;
                }
                dialog.dismiss();
            }
        });

        Button confirmScore = (Button) dialog.findViewById(R.id.score_picker_confirm);
        confirmScore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                computeScores(numberPicker.getValue());
                dialog.dismiss();
            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.score_picker_cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
