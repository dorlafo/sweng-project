package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ch.epfl.sweng.jassatepfl.model.Round;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;

public class HistoryActivity extends BaseActivityWithNavDrawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

    }

    public void show(MatchStats matchStats) {
        /*
        xxx.setContentView(R.layout.score_table_layout);

        TableLayout tableLayout = (TableLayout) xxx.findViewById(R.id.score_table_layout);
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
        */
    }

}
