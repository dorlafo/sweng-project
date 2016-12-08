package ch.epfl.sweng.jassatepfl.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, Boolean> playersReady;

    public PlayerListAdapter(Context context, int resource, List<Player> players, Match match, Map<String, Boolean> playersReady) {
        super(context, resource, players);
        this.players = players;
        this.match = match;
        this.playersReady = playersReady;
    }

    public PlayerListAdapter(Context context, int resource, List<Player> players) {
        this(context, resource, players, null, new HashMap<String, Boolean>());
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

        TextView quoteTv = (TextView) convertView.findViewById(R.id.player_quote);
        TextView playerName = (TextView) convertView.findViewById(R.id.player_name);
        if(match == null) {
            playerName.setText(p.toString());
            quoteTv.setText(String.format(getContext().getString(R.string.profile_label_quote), p.getQuote()));
        }
        else {
            quoteTv.setText(Integer.toString(p.getQuote()));
            if (match.teamNbForPlayer(p) == -1){
                playerName.setText(getFirstFirstName(p.getFirstName()) + " : no team assigned yet");
            }
            else {
                playerName.setText(getFirstFirstName(p.getFirstName()) + " : team " + (match.teamNbForPlayer(p)+1));
            }

            if(playersReady.containsKey(p.getID().toString()) && playersReady.get(p.getID().toString())) {
                playerName.setBackgroundColor(0xFF00FF00);
            }
            else {
                playerName.setBackgroundColor(0xFFFFFFFF);
            }
        }


        return convertView;
    }

    private String getFirstFirstName(String name) {
        int indexSpace = name.indexOf(' ');
        if(indexSpace == -1) {
            return name;
        }
        else {
            return name.substring(0, indexSpace);
        }
    }

    public void refreshData(List<Player> p, Match m, Map<String, Boolean> pr) {
        this.players.clear();
        this.players.addAll(p);
        this.match = m;
        this.playersReady.clear();
        this.playersReady.putAll(pr);
        notifyDataSetChanged();
    }

}
