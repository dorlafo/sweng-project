package ch.epfl.sweng.jassatepfl;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static ch.epfl.sweng.jassatepfl.GameActivity.Caller.FIRST_TEAM;
import static ch.epfl.sweng.jassatepfl.GameActivity.Caller.SECOND_TEAM;

public class GameActivity extends BaseAppCompatActivity implements
        OnClickListener, OnItemSelectedListener {

    // TODO: max points depending on variant?
    private final static int TOTAL_POINTS_IN_ROUND = 157;
    private final static int MATCH_POINTS = 257;

    private Match currentMatch;
    private MatchStats matchStats;
    private String matchId;
    private int pointsGoal;

    private TextView firstTeamScoreDisplay;
    private TextView secondTeamScoreDisplay;

    protected enum Caller {FIRST_TEAM, SECOND_TEAM}

    private Caller caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Intent intent = getIntent();
        //String matchId = intent.getStringExtra("match_Id");
        matchId = "-KXIHjko1keaaJX8iqhy";
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

        pointsGoal = currentMatch.getGameVariant().getPointGoal();
        final boolean isOwner = currentMatch.createdBy().getID().toString().equals(fAuth.getCurrentUser().getDisplayName());
        final int visibility = isOwner ? VISIBLE : INVISIBLE;

        firstTeamScoreDisplay = (TextView) findViewById(R.id.score_display_1);
        secondTeamScoreDisplay = (TextView) findViewById(R.id.score_display_2);

        ImageButton firstTeamUpdateButton = (ImageButton) findViewById(R.id.score_update_1);
        firstTeamUpdateButton.setOnClickListener(this);
        firstTeamUpdateButton.setEnabled(isOwner);
        firstTeamUpdateButton.setVisibility(visibility);
        ImageButton secondTeamUpdateButton = (ImageButton) findViewById(R.id.score_update_2);
        secondTeamUpdateButton.setOnClickListener(this);
        secondTeamUpdateButton.setEnabled(isOwner);
        secondTeamUpdateButton.setVisibility(visibility);

        ArrayAdapter<Meld> meldAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Meld.values());
        meldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner firstMeldSpinner = (Spinner) findViewById(R.id.score_meld_spinner_1);
        firstMeldSpinner.setAdapter(meldAdapter);
        firstMeldSpinner.setOnItemSelectedListener(this);
        firstMeldSpinner.setEnabled(isOwner);
        firstMeldSpinner.setVisibility(visibility);

        Spinner secondMeldSpinner = (Spinner) findViewById(R.id.score_meld_spinner_2);
        secondMeldSpinner.setAdapter(meldAdapter);
        secondMeldSpinner.setOnItemSelectedListener(this);
        secondMeldSpinner.setEnabled(isOwner);
        secondMeldSpinner.setVisibility(visibility);

        TextView goal = (TextView) findViewById(R.id.game_playing_to);
        String goalText = String.format(getString(R.string.game_text_point_goal), pointsGoal);
        goal.setText(goalText);

        Button nextRoundButton = (Button) findViewById(R.id.game_button_next_round);
        nextRoundButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                matchStats.addRound();
                updateMatchStats();
            }
        });
        nextRoundButton.setEnabled(isOwner);
        nextRoundButton.setVisibility(visibility);

        if (!isOwner) {
            dbRefWrapped.child("stats").child("matchStats").child(matchId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            MatchStats modifiedMatchStats = dataSnapshot.getValue(MatchStats.class);
                            displayScore(modifiedMatchStats);
                            if (modifiedMatchStats.goalHasBeenReached()) {
                                displayEndOfMatchMessage();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("ERROR-DATABASE", databaseError.toString());
                        }
                    });
        }

        displayScore(matchStats);
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
        switch (parent.getId()) {
            case R.id.score_meld_spinner_1:
                matchStats.setMeld(0, meld);
                break;
            case R.id.score_meld_spinner_2:
                matchStats.setMeld(1, meld);
                break;
        }
        displayScore(matchStats);
        updateMatchStats();
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
        matchStats.setScore(new int[]{firstTeamScore, secondTeamScore});
        displayScore(matchStats);
        updateMatchStats();
        if (matchStats.goalHasBeenReached()) {
            dbRefWrapped.child("stats").child("buffer").child(matchId).setValue(matchStats);
            displayEndOfMatchMessage();
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayScore(MatchStats stats) {
        Integer firstTeamScore = stats.getCurrentRoundTeamScore(0);
        Integer secondTeamScore = stats.getCurrentRoundTeamScore(1);
        firstTeamScoreDisplay.setText(firstTeamScore.toString());
        secondTeamScoreDisplay.setText(secondTeamScore.toString());
    }

    private void showScorePicker() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.game_text_score_picker);
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
                displayScore(matchStats);
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

    private void displayEndOfMatchMessage() {
        String winningTeam = "Team " + (matchStats.getWinnerIndex() + 1);
        String message = String.format(getString(R.string.dialog_game_end), winningTeam);
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton(R.string.dialog_leave, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(GameActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void updateMatchStats() {
        dbRefWrapped.child("stats").child("matchStats").child(matchId).setValue(matchStats);
    }

}
