package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Round;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static ch.epfl.sweng.jassatepfl.tools.DatabaseUtils.DATABASE_STATS;
import static ch.epfl.sweng.jassatepfl.tools.DatabaseUtils.DATABASE_STATS_MATCH_STATS_ARCHIVE;

public class HistoryActivity extends BaseActivityWithNavDrawer {

    private MatchStats matchStats;
    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        } else {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_history, drawer, false);
            drawer.addView(contentView, 0);

            Intent startingIntent = getIntent();
            String matchId = startingIntent.getStringExtra("match_Id");

            dbRefWrapped.child(DATABASE_STATS).child(DATABASE_STATS_MATCH_STATS_ARCHIVE)
                    .child(matchId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    matchStats = dataSnapshot.getValue(MatchStats.class);
                    if (matchStats != null) {
                        match = matchStats.getMatch();
                        displayMatchHistory();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void displayMatchHistory() {
        TextView firstTeamMembers = (TextView) findViewById(R.id.history_team_members_1);
        firstTeamMembers.setText(teamToString(0));

        TextView secondTeamMembers = (TextView) findViewById(R.id.history_team_members_2);
        secondTeamMembers.setText(teamToString(1));

        int winner = matchStats.getWinnerIndex();
        TextView firstWinner = (TextView) findViewById(R.id.history_winner_1);
        firstWinner.setVisibility(winner == 0 ? VISIBLE : INVISIBLE);
        TextView secondWinner = (TextView) findViewById(R.id.history_winner_2);
        secondWinner.setVisibility(winner == 1 ? VISIBLE : INVISIBLE);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableLayout firstTeamScoreTable = (TableLayout) findViewById(R.id.history_table_layout_1);
        TableLayout secondTeamScoreTable = (TableLayout) findViewById(R.id.history_table_layout_2);

        int roundIndex = 1;
        int black = ContextCompat.getColor(this, android.R.color.black);

        for (Round round : matchStats.getRounds()) {
            TableRow firstTeamRow = (TableRow) inflater.inflate(R.layout.score_table_row, firstTeamScoreTable, false);
            TableRow secondTeamRow = (TableRow) inflater.inflate(R.layout.score_table_row, secondTeamScoreTable, false);


            TextView firstRoundIndexView = (TextView) firstTeamRow.findViewById(R.id.score_table_row_round_index);
            firstRoundIndexView.setTextColor(black);
            firstRoundIndexView.setText(String.format(java.util.Locale.getDefault(), "%d", roundIndex));

            TextView secondRoundIndexView = (TextView) secondTeamRow.findViewById(R.id.score_table_row_round_index);
            secondRoundIndexView.setTextColor(black);
            secondRoundIndexView.setText(String.format(java.util.Locale.getDefault(), "%d", roundIndex));
            ++roundIndex;

            TextView firstScore = (TextView) firstTeamRow.findViewById(R.id.score_table_row_points);
            firstScore.setTextColor(black);
            firstScore.setText(String.format(java.util.Locale.getDefault(), "%d", round.getTeamTotalScore(0)));

            TextView secondScore = (TextView) secondTeamRow.findViewById(R.id.score_table_row_points);
            secondScore.setTextColor(black);
            secondScore.setText(String.format(java.util.Locale.getDefault(), "%d", round.getTeamTotalScore(1)));

            TextView firstMelds = (TextView) firstTeamRow.findViewById(R.id.score_table_row_melds);
            firstMelds.setTextColor(black);
            firstMelds.setText(round.meldsToString(0));

            TextView secondMelds = (TextView) secondTeamRow.findViewById(R.id.score_table_row_melds);
            secondMelds.setTextColor(black);
            secondMelds.setText(round.meldsToString(1));

            firstTeamScoreTable.addView(firstTeamRow);
            secondTeamScoreTable.addView(secondTeamRow);
        }
    }

    private String teamToString(int teamIndex) {
        List<String> teamIds = match.getTeams().get("Team" + teamIndex);
        StringBuilder builder = new StringBuilder();
        for (Iterator<String> iterator = teamIds.iterator(); iterator.hasNext(); ) {
            String id = iterator.next();
            builder.append(match.getPlayerById(id).getFirstName().split(" ")[0]);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

}
