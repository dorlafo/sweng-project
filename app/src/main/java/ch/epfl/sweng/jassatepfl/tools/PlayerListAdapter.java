package ch.epfl.sweng.jassatepfl.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;

/**
 * Adapter for Player List
 *
 * @author Alexis Montavon
 */
public class PlayerListAdapter extends ArrayAdapter<Player> {

    private List<Player> players;
    private Match match;

    public PlayerListAdapter(Context context, int resource, List<Player> players, Match match) {
        super(context, resource, players);
        this.players = players;
        this.match = match;
    }

    public PlayerListAdapter(Context context, int resource, List<Player> players) {
        this(context, resource, players, null);
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Player getItem(int position) {
        return players.get(position);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.player_list_element, null);
        }

        Player p = getItem(position);

        TextView playerName = (TextView) convertView.findViewById(R.id.player_name);
        if(match == null) {
            playerName.setText(p.toString());
        }
        else if (match.teamNbForPlayer(p) == -1){
            playerName.setText(p.toString() + " : no team assigned yet");
        }
        else {
            playerName.setText(p.toString() + " : team " + match.teamNbForPlayer(p));
        }


        return convertView;
    }

}
