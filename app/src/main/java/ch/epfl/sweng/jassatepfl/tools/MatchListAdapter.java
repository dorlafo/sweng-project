package ch.epfl.sweng.jassatepfl.tools;


import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;

import ch.epfl.sweng.jassatepfl.BaseActivityWithNavDrawer;
import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.model.Match;

/**
 * Custom {@link android.widget.Adapter Adapter} used to display
 * matches in the {@link android.widget.ListView ListView} of
 * {@link ch.epfl.sweng.jassatepfl.MatchListActivity MatchListActivity}.
 */
public class MatchListAdapter extends FirebaseListAdapter<Match> {

    private final Activity activity;

    public MatchListAdapter(Activity activity) {
        super(activity, Match.class, R.layout.match_list_row,
                ((BaseActivityWithNavDrawer) activity).dbRefWrapped.child("matches")
                        .orderByChild("privateMatch").equalTo(false));
        this.activity = activity;
    }

    @Override
    protected void populateView(View v, Match match, int position) {
        MatchStringifier stringifier = new MatchStringifier(activity);
        stringifier.setMatch(match);

        TextView description = (TextView) v.findViewById(R.id.description);
        description.setText(match.getDescription());

        TextView rankData = (TextView) v.findViewById(R.id.rank_data);
        rankData.setText(stringifier.rankToString());

        TextView players = (TextView) v.findViewById(R.id.players_data);
        players.setText(stringifier.playersToString());

        TextView variant = (TextView) v.findViewById(R.id.variant_data);
        variant.setText(stringifier.variantToString());

        TextView expirationDate = (TextView) v.findViewById(R.id.expiration_date_data);
        expirationDate.setText(stringifier.dateToStringCustom());
    }

}
