package ch.epfl.sweng.jassatepfl;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Match.Meld;
import ch.epfl.sweng.jassatepfl.model.Round;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static ch.epfl.sweng.jassatepfl.GameActivity.Caller.FIRST_TEAM;
import static ch.epfl.sweng.jassatepfl.GameActivity.Caller.SECOND_TEAM;
import static ch.epfl.sweng.jassatepfl.GameActivity.Mode.OFFLINE;
import static ch.epfl.sweng.jassatepfl.GameActivity.Mode.ONLINE;
import static ch.epfl.sweng.jassatepfl.GameActivity.PickerMode.COMMON_GOAL;
import static ch.epfl.sweng.jassatepfl.GameActivity.PickerMode.FIRST_TEAM_GOAL;
import static ch.epfl.sweng.jassatepfl.GameActivity.PickerMode.SCORE;
import static ch.epfl.sweng.jassatepfl.GameActivity.PickerMode.SECOND_TEAM_GOAL;
import static ch.epfl.sweng.jassatepfl.GameActivity.SplitMode.NORMAL;
import static ch.epfl.sweng.jassatepfl.GameActivity.SplitMode.SPLIT;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.SENTINEL;
import static ch.epfl.sweng.jassatepfl.tools.DatabaseUtils.DATABASE_MATCH_STATS;
import static ch.epfl.sweng.jassatepfl.tools.DatabaseUtils.DATABASE_STATS;
import static ch.epfl.sweng.jassatepfl.tools.DatabaseUtils.DATABASE_STATS_BUFFER;

public class GameActivity extends BaseActivityWithNavDrawer implements OnClickListener {

    private static final String TAG = WaitingPlayersActivity.class.getSimpleName();

    private final static int TOTAL_POINTS_IN_ROUND = 157;
    private final static int MATCH_POINTS = 257;

    private String matchId;
    private Match currentMatch;
    private MatchStats matchStats;

    private ValueEventListener statsListener;

    private TextView firstTeamScoreDisplay;
    private TextView secondTeamScoreDisplay;
    private ImageButton cancelButton;

    private Caller caller;
    private Stack<Caller> meldCallers;
    private Mode mode;
    private SplitMode splitMode;

    private int scoreMultiplier;

    private Toast toast;

    protected enum Caller {FIRST_TEAM, SECOND_TEAM}

    protected enum Mode {ONLINE, OFFLINE}

    protected enum PickerMode {SCORE, COMMON_GOAL, FIRST_TEAM_GOAL, SECOND_TEAM_GOAL}

    protected enum SplitMode {NORMAL, SPLIT}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent startingIntent = getIntent();
        mode = startingIntent.getStringExtra("mode").equals("online") ? ONLINE : OFFLINE;
        splitMode = NORMAL;
        if (mode == ONLINE && fAuth.getCurrentUser() == null) {
            //Log.d(TAG, "showLogin:getCurrentUser:null");
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        } else {
            //Log.d(TAG, "showLogin:getCurrentUser:notNull");

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_game, drawer, false);
            drawer.addView(contentView, 0);

            //Sentinel matchStats to avoid null pointer exception
            matchStats = new MatchStats(Match.sentinelMatch());

            meldCallers = new Stack<>();

            Intent intent = getIntent();
            matchId = intent.getStringExtra("match_Id");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mode == ONLINE) {
            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES).child(matchId)
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
        } else {
            currentMatch = Match.sentinelMatch();
            setUp();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (statsListener != null) {
            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCH_STATS).child(matchId)
                    .removeEventListener(statsListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score_display_1:
                displayScoreHistory(0);
                break;
            case R.id.score_display_2:
                displayScoreHistory(1);
                break;
            case R.id.score_update_1:
                caller = FIRST_TEAM;
                showNumPadScorePicker(SCORE);
                break;
            case R.id.score_update_2:
                caller = SECOND_TEAM;
                showNumPadScorePicker(SCORE);
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
                    displayScore();
                    updateMatchStats();
                }
                break;
            case R.id.game_playing_to:
                showNumPadScorePicker(COMMON_GOAL);
                break;
            case R.id.split_team_goals:
                changeSplitMode();
                break;
            case R.id.team_goal_1:
                showNumPadScorePicker(FIRST_TEAM_GOAL);
                break;
            case R.id.team_goal_2:
                showNumPadScorePicker(SECOND_TEAM_GOAL);
                break;
        }
    }

    private void changeSplitMode() {
        TextView commonGoal = (TextView) findViewById(R.id.game_playing_to);
        TextView firstTeamGoal = (TextView) findViewById(R.id.team_goal_1);
        TextView secondTeamGoal = (TextView) findViewById(R.id.team_goal_2);
        if (splitMode == NORMAL) {
            splitMode = SPLIT;
            commonGoal.setVisibility(INVISIBLE);
            String defaultGoal = Integer.toString(currentMatch.getGameVariant().getPointGoal());

            firstTeamGoal.setVisibility(VISIBLE);
            firstTeamGoal.setText(defaultGoal);
            firstTeamGoal.setOnClickListener(this);

            secondTeamGoal.setVisibility(VISIBLE);
            secondTeamGoal.setText(defaultGoal);
            secondTeamGoal.setOnClickListener(this);
        } else {
            splitMode = NORMAL;
            commonGoal.setVisibility(VISIBLE);
            firstTeamGoal.setVisibility(GONE);
            secondTeamGoal.setVisibility(GONE);
        }
    }

    private void updateSplitGoal(int index, int points) {
        int goalIndex = index == 0 ? R.id.team_goal_1 : R.id.team_goal_2;
        TextView goal = (TextView) findViewById(goalIndex);
        goal.setText(String.format(java.util.Locale.getDefault(), "%d", points));
    }

    private void displayMeldSpinner(final int teamIndex) {
        List<Meld> melds = new ArrayList<>(Arrays.asList(Meld.values()));
        melds.remove(SENTINEL);
        final ArrayAdapter<Meld> meldAdapter = new ArrayAdapter<>(this, R.layout.meld_spinner, melds);
        new AlertDialog.Builder(this)
                //.setTitle(R.string.game_select_meld)
                .setAdapter(meldAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Meld meld = meldAdapter.getItem(which);
                        matchStats.setMeld(teamIndex, meld);
                        dialog.dismiss();
                        displayScore();
                        updateMatchStats();
                        cancelButton.setEnabled(true);
                    }
                })
                .create().show();
    }

    private void computeScores(int callerScore, int scoreMultiplier) {
        int otherTeamScore = TOTAL_POINTS_IN_ROUND * scoreMultiplier - callerScore;
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
        displayScore();
        if (matchStats.goalHasBeenReached()) {
            if (matchStats.allTeamsHaveReachedGoal()) {
                matchStats.setWinnerIndex(caller.ordinal());
            }
            if (mode == ONLINE) {
                dbRefWrapped.child(DATABASE_STATS).child(DATABASE_STATS_BUFFER).child(matchId).setValue(matchStats);
            }
            displayEndOfMatchMessage(matchStats.getWinnerIndex());
        }
        updateMatchStats();
    }

    @SuppressLint("SetTextI18n")
    private void displayScore() {
        Integer firstTeamScore = matchStats.obtainTotalMatchScore(0);
        Integer secondTeamScore = matchStats.obtainTotalMatchScore(1);
        firstTeamScoreDisplay.setText(firstTeamScore.toString());
        secondTeamScoreDisplay.setText(secondTeamScore.toString());
    }

    private void showNumPadScorePicker(final PickerMode pickerMode) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.score_picker_numpad);

        TextView title = (TextView) dialog.findViewById(R.id.score_picker_title);
        title.setText(pickerMode == SCORE ? R.string.game_text_score_picker : R.string.game_text_goal_picker);

        final TextView pointsDisplay = (TextView) dialog.findViewById(R.id.score_picker_score_display);
        pointsDisplay.setText("0");

        ViewGroup checkboxLayout = (ViewGroup) dialog.findViewById(R.id.score_picker_checkbox_layout);
        checkboxLayout.setVisibility(pickerMode == SCORE ? VISIBLE : INVISIBLE);

        scoreMultiplier = 1;
        CheckBox doubleScore = (CheckBox) dialog.findViewById(R.id.numpad_double_score);
        doubleScore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scoreMultiplier = isChecked ? 2 : 1;
            }
        });

        Button numpad0 = (Button) dialog.findViewById(R.id.numpad_0);
        numpad0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, 0, pickerMode);
            }
        });

        Button numpad1 = (Button) dialog.findViewById(R.id.numpad_1);
        numpad1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, 1, pickerMode);
            }
        });

        Button numpad2 = (Button) dialog.findViewById(R.id.numpad_2);
        numpad2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, 2, pickerMode);
            }
        });

        Button numpad3 = (Button) dialog.findViewById(R.id.numpad_3);
        numpad3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, 3, pickerMode);
            }
        });

        Button numpad4 = (Button) dialog.findViewById(R.id.numpad_4);
        numpad4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, 4, pickerMode);
            }
        });

        Button numpad5 = (Button) dialog.findViewById(R.id.numpad_5);
        numpad5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, 5, pickerMode);
            }
        });

        Button numpad6 = (Button) dialog.findViewById(R.id.numpad_6);
        numpad6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, 6, pickerMode);
            }
        });

        Button numpad7 = (Button) dialog.findViewById(R.id.numpad_7);
        numpad7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, 7, pickerMode);
            }
        });

        Button numpad8 = (Button) dialog.findViewById(R.id.numpad_8);
        numpad8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, 8, pickerMode);
            }
        });

        Button numpad9 = (Button) dialog.findViewById(R.id.numpad_9);
        numpad9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, 9, pickerMode);
            }
        });

        ImageButton numpadCorrect = (ImageButton) dialog.findViewById(R.id.numpad_correct);
        numpadCorrect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScorePickerDisplay(pointsDisplay, -1, pickerMode);
            }
        });

        Button match = (Button) dialog.findViewById(R.id.score_picker_match);
        match.setVisibility(pickerMode == SCORE ? VISIBLE : GONE);
        match.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (caller) {
                    case FIRST_TEAM:
                        updateScore(MATCH_POINTS * scoreMultiplier, 0);
                        break;
                    case SECOND_TEAM:
                        updateScore(0, MATCH_POINTS * scoreMultiplier);
                        break;
                }
                dialog.dismiss();
                displayScore();
            }
        });

        Button confirmScore = (Button) dialog.findViewById(R.id.score_picker_confirm);
        confirmScore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int points = Integer.parseInt(pointsDisplay.getText().toString());
                switch (pickerMode) {
                    case SCORE:
                        computeScores(points * scoreMultiplier, scoreMultiplier);
                        break;
                    case COMMON_GOAL:
                        matchStats.updatePointsGoal(points);
                        updatePointsGoal(points);
                        break;
                    case FIRST_TEAM_GOAL:
                        matchStats.updatePointsGoal(0, points);
                        updateSplitGoal(0, points);
                        break;
                    case SECOND_TEAM_GOAL:
                        matchStats.updatePointsGoal(1, points);
                        updateSplitGoal(1, points);
                        break;
                }
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

    private void updateScorePickerDisplay(TextView scoreDisplay, int value, PickerMode pickerMode) {
        String currentDisplay = scoreDisplay.getText().toString();
        if (value == -1) {
            if (currentDisplay.length() <= 1) {
                currentDisplay = "0";
            } else {
                currentDisplay = currentDisplay.substring(0, currentDisplay.length() - 1);
            }
        } else if (currentDisplay.equals("0") && value != 0) {
            currentDisplay = Integer.toString(value);
        } else if (!currentDisplay.equals("0")) {
            currentDisplay += value;
        }

        int displayedPoints = Integer.parseInt(currentDisplay);
        if (pickerMode == SCORE) {
            if (displayedPoints <= TOTAL_POINTS_IN_ROUND) {
                scoreDisplay.setText(currentDisplay);
            } else {
                displayToast(R.string.toast_invalid_score, TOTAL_POINTS_IN_ROUND);
            }
        } else {
            scoreDisplay.setText(currentDisplay);
        }
    }

    private void displayEndOfMatchMessage(int winningTeamIndex) {
        String winningTeam = "Team " + (winningTeamIndex + 1);
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
                .setCancelable(false)
                .show();
    }

    private void updateMatchStats() {
        if (mode == ONLINE) {
            dbRefWrapped.child(DATABASE_MATCH_STATS).child(matchId).setValue(matchStats);
        }
    }

    private void setUp() {
        firstTeamScoreDisplay = (TextView) findViewById(R.id.score_display_1);
        firstTeamScoreDisplay.setOnClickListener(this);
        firstTeamScoreDisplay.setEnabled(false);

        secondTeamScoreDisplay = (TextView) findViewById(R.id.score_display_2);
        secondTeamScoreDisplay.setOnClickListener(this);
        secondTeamScoreDisplay.setEnabled(false);

        final boolean isOwner = mode == OFFLINE || currentMatch.createdBy().getID().toString().equals(getUserSciper());
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

        ImageButton splitGoals = (ImageButton) findViewById(R.id.split_team_goals);
        splitGoals.setVisibility(mode == OFFLINE ? VISIBLE : INVISIBLE);
        splitGoals.setOnClickListener(this);

        cancelButton = (ImageButton) findViewById(R.id.score_update_cancel);
        cancelButton.setOnClickListener(this);
        cancelButton.setVisibility(visibility);

        updatePointsGoal(currentMatch.getGameVariant().getPointGoal());

        //TODO: check if this code do what was intended
        if (isOwner) {
            firstTeamScoreDisplay.setEnabled(true);
            secondTeamScoreDisplay.setEnabled(true);
        }

        if (mode == ONLINE) {
            TextView firstTeamMembers = (TextView) findViewById(R.id.team_members_1);
            firstTeamMembers.setText(teamToString(0));
            firstTeamMembers.setVisibility(VISIBLE);

            TextView secondTeamMembers = (TextView) findViewById(R.id.team_members_2);
            secondTeamMembers.setText(teamToString(1));
            secondTeamMembers.setVisibility(VISIBLE);

            statsListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    matchStats = dataSnapshot.getValue(MatchStats.class);
                    if (matchStats != null) {
                        firstTeamScoreDisplay.setEnabled(true);
                        secondTeamScoreDisplay.setEnabled(true);
                        displayScore();
                        if (matchStats.goalHasBeenReached()) {
                            displayEndOfMatchMessage(matchStats.getWinnerIndex());
                        }
                    } else {
                        firstTeamScoreDisplay.setEnabled(false);
                        secondTeamScoreDisplay.setEnabled(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ERROR-DATABASE", databaseError.toString());
                }
            };
            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCH_STATS).child(matchId)
                    .addValueEventListener(statsListener);
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayScoreHistory(int teamIndex) {
        final Dialog dialog = new Dialog(this) {
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent event) {
                this.dismiss();
                return true;
            }
        };
        dialog.setContentView(R.layout.score_table_layout);

        TableLayout tableLayout = (TableLayout) dialog.findViewById(R.id.score_table_layout);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int roundIndex = 1;

        for (Round round : matchStats.getRounds()) {
            TableRow row = (TableRow) inflater.inflate(R.layout.score_table_row, tableLayout, false);

            int black = ContextCompat.getColor(this, android.R.color.black);

            TextView roundIndexView = (TextView) row.findViewById(R.id.score_table_row_round_index);
            roundIndexView.setTextColor(black);
            roundIndexView.setText(Integer.toString(roundIndex));
            ++roundIndex;

            TextView score = (TextView) row.findViewById(R.id.score_table_row_points);
            score.setTextColor(black);
            score.setText(round.getTeamTotalScore(teamIndex).toString());

            TextView melds = (TextView) row.findViewById(R.id.score_table_row_melds);
            melds.setTextColor(black);
            melds.setText(round.meldsToString(teamIndex));

            tableLayout.addView(row);
        }

        dialog.show();
    }

    private String teamToString(int teamIndex) {
        List<String> teamIds = currentMatch.getTeams().get("Team" + teamIndex);
        StringBuilder builder = new StringBuilder();
        for (Iterator<String> iterator = teamIds.iterator(); iterator.hasNext(); ) {
            String id = iterator.next();
            builder.append(currentMatch.getPlayerById(id).getFirstName().split(" ")[0]);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private void updatePointsGoal(int pointsGoal) {
        TextView goal = (TextView) findViewById(R.id.game_playing_to);
        String goalText = String.format(getString(R.string.game_text_point_goal),
                "<b>" + pointsGoal + "</b>");
        goal.setText(Html.fromHtml(goalText));
        if (mode == OFFLINE) {
            goal.setEnabled(true);
            goal.setOnClickListener(this);
        }
    }

    private void displayToast(int stringId, int points) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, String.format(getString(stringId),
                points), Toast.LENGTH_SHORT);
        toast.show();
    }

}
