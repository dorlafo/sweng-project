package ch.epfl.sweng.project.tools;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.model.Player;



/**
 * Created by leo on 07.11.16.
 */

/**
 * Adapter that display the players of the match in parameter.
 */

public class PlayerListAdapterForMatch extends FirebaseListAdapter<Player> {

    private final Activity activity;

    public PlayerListAdapterForMatch(Activity activity, String matchID) {
        super(activity, Player.class, R.layout.list_element_player,
                FirebaseDatabase.getInstance().getReference().child("matches").child(matchID).child("players"));
        this.activity = activity;
    }



    @Override
    protected void populateView(View v, Player player, int position) {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView rank = (TextView) v.findViewById(R.id.rank_data);
            TextView playerID = (TextView) v.findViewById(R.id.player_id_data);

            name.setText(player.getFirstName() + " " + player.getLastName());
            rank.setText(player.getRank().toString());
            playerID.setText(player.getID().toString());

    }
}