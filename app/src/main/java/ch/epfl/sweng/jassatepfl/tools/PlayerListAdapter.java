package ch.epfl.sweng.jassatepfl.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;

public class PlayerListAdapter extends ArrayAdapter<Player> {

    private final Context context;
    private final List<Player> players;
    private Match match;
    private final Map<String, Boolean> playersReady;

    public PlayerListAdapter(Context context, int resource, List<Player> players, Match match, Map<String, Boolean> playersReady) {
        super(context, resource, players);
        this.context = context;
        this.players = new ArrayList<>(players);
        this.match = match;
        this.playersReady = new HashMap<>(playersReady);
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

    @SuppressLint("SetTextI18n")
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.player_list_element, parent, false);
        }

        Player p = getItem(position);

        TextView quoteTv = (TextView) convertView.findViewById(R.id.player_quote);
        TextView playerName = (TextView) convertView.findViewById(R.id.player_name);
        quoteTv.setText(String.format(getContext().getString(R.string.player_list_adapter_quote), p.getQuote()));
        if (match == null) {
            playerName.setText(p.toString());

        } else {
            Resources res = context.getResources();
            String firstFirstName = p.getFirstName().split(" ")[0];
            String teamAssignment = match.teamNbForPlayer(p) == -1 ?
                    String.format(res.getString(R.string.wait_not_assigned), firstFirstName) :
                    String.format(res.getString(R.string.wait_assigned), firstFirstName, (match.teamNbForPlayer(p) + 1));
            playerName.setText(teamAssignment);

            View layout = convertView.findViewById(R.id.player_list_layout);
            if (playersReady.containsKey(p.getID().toString()) && playersReady.get(p.getID().toString())) {
                layout.setBackgroundColor(0xFF00FF00);
            } else {
                layout.setBackgroundColor(0xFFFFFFFF);
            }
        }

        return convertView;
    }

    public void refreshData(List<Player> p, Match m, Map<String, Boolean> pr) {
        this.players.clear();
        this.players.addAll(p);
        this.match = m;
        this.playersReady.clear();
        this.playersReady.putAll(pr);
        notifyDataSetChanged();
    }

    public void refreshData(List<Player> p) {
        this.players.clear();
        this.players.addAll(p);
        notifyDataSetChanged();
    }

}
