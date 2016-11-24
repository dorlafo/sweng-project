package ch.epfl.sweng.jassatepfl;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;

import static ch.epfl.sweng.jassatepfl.GameActivity.Caller.FIRST_TEAM;
import static ch.epfl.sweng.jassatepfl.GameActivity.Caller.SECOND_TEAM;

public class GameActivity extends BaseAppCompatActivity implements
        OnClickListener, OnValueChangeListener {

    private final static int TOTAL_POINTS_IN_ROUND = 157;

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

        //matchStats = new MatchStats(matchId, currentMatch.getGameVariant(), teamList); TODO: ce truc
    }

    @Override
    public void onClick(View v) {
        // TODO: numberPicker
        switch (v.getId()) {
            case R.id.score_update_1:
                caller = FIRST_TEAM;
                break;
            case R.id.score_update_2:
                caller = SECOND_TEAM;
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        // Check if there was match
        int callerScore = newVal == TOTAL_POINTS_IN_ROUND ? TOTAL_POINTS_IN_ROUND + 100 : newVal;
        int otherTeamScore = TOTAL_POINTS_IN_ROUND - newVal;
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

}
