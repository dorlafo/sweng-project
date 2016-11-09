package ch.epfl.sweng.jassatepfl.tools;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.model.Match;

public class MatchListAdapter extends FirebaseListAdapter<Match> {

    private final Activity activity;

    public MatchListAdapter(Activity activity) {
        super(activity, Match.class, R.layout.match_list_row,
                FirebaseDatabase.getInstance().getReference("matches")
                        .child("privateMatch").equalTo("false"));
        this.activity = activity;
    }

    @Override
    protected void populateView(View v, Match match, int position) {
        TextView description = (TextView) v.findViewById(R.id.description);
        TextView rankData = (TextView) v.findViewById(R.id.rank_data);
        TextView players = (TextView) v.findViewById(R.id.players_data);
        TextView variant = (TextView) v.findViewById(R.id.variant_data);
        TextView expirationDate = (TextView) v.findViewById(R.id.expiration_date_data);

        MatchStringifier stringifier = new MatchStringifier(activity);
        stringifier.setMatch(match);
        description.setText(match.getDescription());
        rankData.setText(stringifier.rankToString());
        players.setText(stringifier.playersToString());
        variant.setText(stringifier.variantToString());
        expirationDate.setText(stringifier.dateToStringCustom());
    }

}
