package ch.epfl.sweng.jassatepfl.tools;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.model.Player;



/**
 * Adapter that display the players of the match in parameter.
 */
public class PlayerListAdapterForMatch extends FirebaseListAdapter<Player> {

    public PlayerListAdapterForMatch(Activity activity, String matchID) {
        super(activity, Player.class, R.layout.list_element_player,
                FirebaseDatabase.getInstance().getReference()
                        .child("matches").child(matchID).child("players"));

    }

    @Override
    protected void populateView(View v, Player player, int position) {
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView rank = (TextView) v.findViewById(R.id.rank_data);

        name.setText(player.getFirstName() + " " + player.getLastName());
        rank.setText(player.getRank().toString());
    }
}
