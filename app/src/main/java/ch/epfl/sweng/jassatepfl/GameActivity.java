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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Stack;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Match.Meld;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static ch.epfl.sweng.jassatepfl.GameActivity.Caller.FIRST_TEAM;
import static ch.epfl.sweng.jassatepfl.GameActivity.Caller.SECOND_TEAM;

public class GameActivity extends BaseAppCompatActivity implements OnClickListener {

    // TODO: max points depending on variant?
    private final static int TOTAL_POINTS_IN_ROUND = 157;
    private final static int MATCH_POINTS = 257;

    private String matchId;
    private Match currentMatch;
    private MatchStats matchStats;

    private ValueEventListener statsListener;

    private TextView firstTeamScoreDisplay;
    private TextView secondTeamScoreDisplay;
    private ImageButton cancelButton;

    protected enum Caller {FIRST_TEAM, SECOND_TEAM}

    private Caller caller;
    private Stack<Caller> meldCallers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        meldCallers = new Stack<>();

        Intent intent = getIntent();
        matchId = intent.getStringExtra("match_Id");
        dbRefWrapped.child("matches").child(matchId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentMatch = dataSnapshot.getValue(Match.class);
                        if (currentMatch != null) {
                            setUp();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("ERROR-DATABASE", databaseError.toString());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (statsListener != null) {
            dbRefWrapped.child("matchStats").child(matchId)
                    .removeEventListener(statsListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score_update_1:
                caller = FIRST_TEAM;
                showScorePicker();
                break;
            case R.id.score_update_2:
                caller = SECOND_TEAM;
                showScorePicker();
                break;
            case R.id.score_meld_spinner_1:
                meldCallers.push(FIRST_TEAM);
                displayMeldSpinner(0);
                break;
            case R.id.score_meld_spinner_2:
                meldCallers.push(SECOND_TEAM);
                displayMeldSpinner(1);
                break;
            case R.id.score_update_cancel:
                if (matchStats.getCurrentRoundIndex() == 0 && !matchStats.meldWasSetThisRound()) {
                    Toast.makeText(this, R.string.toast_cannot_cancel, Toast.LENGTH_SHORT)
                            .show();
                    cancelButton.setEnabled(false);
                } else {
                    int teamIndex = matchStats.meldWasSetThisRound() ? meldCallers.pop().ordinal() : 0;
                    matchStats.cancelLastRound(teamIndex);
                    displayScore(matchStats);
                    updateMatchStats();
                }
                break;
        }
    }

    private void displayMeldSpinner(final int teamIndex) {
        final ArrayAdapter<Meld> meldAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Meld.values());
        new AlertDialog.Builder(this)
                .setTitle(R.string.game_select_meld)
                .setAdapter(meldAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Meld meld = meldAdapter.getItem(which);
                        matchStats.setMeld(teamIndex, meld);
                        dialog.dismiss();
                        displayScore(matchStats);
                        updateMatchStats();
                        cancelButton.setEnabled(true);
                    }
                })
                .create().show();
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
        matchStats.setScore(0, firstTeamScore);
        matchStats.setScore(1, secondTeamScore);
        matchStats.finishRound();
        cancelButton.setEnabled(true);
        displayScore(matchStats);
        updateMatchStats();
        if (matchStats.goalHasBeenReached()) {
            // dbRefWrapped.child("stats").child("buffer").child(matchId).setValue(matchStats); TODO: mock the buffer
            displayEndOfMatchMessage();
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayScore(MatchStats stats) {
        Integer firstTeamScore = stats.getTotalMatchScore(0);
        Integer secondTeamScore = stats.getTotalMatchScore(1);
        firstTeamScoreDisplay.setText(firstTeamScore.toString());
        secondTeamScoreDisplay.setText(secondTeamScore.toString());
    }

    private void showScorePicker() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.score_picker);
        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.score_picker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(TOTAL_POINTS_IN_ROUND);
        numberPicker.setWrapSelectorWheel(true);

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
                        finish();
                    }
                })
                .show();
    }

    private void updateMatchStats() {
        dbRefWrapped.child("matchStats").child(matchId).setValue(matchStats);
    }

    private void setUp() {
        firstTeamScoreDisplay = (TextView) findViewById(R.id.score_display_1);
        secondTeamScoreDisplay = (TextView) findViewById(R.id.score_display_2);

        final boolean isOwner = currentMatch.createdBy().getID().toString().equals(fAuth.getCurrentUser().getDisplayName());
        final int visibility = isOwner ? VISIBLE : INVISIBLE;

        ImageButton firstTeamUpdateButton = (ImageButton) findViewById(R.id.score_update_1);
        firstTeamUpdateButton.setOnClickListener(this);
        firstTeamUpdateButton.setVisibility(visibility);

        ImageButton secondTeamUpdateButton = (ImageButton) findViewById(R.id.score_update_2);
        secondTeamUpdateButton.setOnClickListener(this);
        secondTeamUpdateButton.setVisibility(visibility);

        ImageButton firstMeldSpinner = (ImageButton) findViewById(R.id.score_meld_spinner_1);
        firstMeldSpinner.setOnClickListener(this);
        firstMeldSpinner.setVisibility(visibility);

        ImageButton secondMeldSpinner = (ImageButton) findViewById(R.id.score_meld_spinner_2);
        secondMeldSpinner.setOnClickListener(this);
        secondMeldSpinner.setVisibility(visibility);

        cancelButton = (ImageButton) findViewById(R.id.score_update_cancel);
        cancelButton.setOnClickListener(this);
        cancelButton.setVisibility(visibility);

        TextView goal = (TextView) findViewById(R.id.game_playing_to);
        int pointsGoal = currentMatch.getGameVariant().getPointGoal();
        String goalText = String.format(getString(R.string.game_text_point_goal), pointsGoal);
        goal.setText(goalText);

        if (isOwner) {
            matchStats = new MatchStats(matchId, currentMatch.getGameVariant());
            displayScore(matchStats);
        } else {
            statsListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MatchStats modifiedMatchStats = dataSnapshot.getValue(MatchStats.class);
                    if (modifiedMatchStats != null) {
                        displayScore(modifiedMatchStats);
                        if (modifiedMatchStats.goalHasBeenReached()) {
                            displayEndOfMatchMessage();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ERROR-DATABASE", databaseError.toString());
                }
            };
            dbRefWrapped.child("matchStats").child(matchId)
                    .addValueEventListener(statsListener);
        }
    }

}
