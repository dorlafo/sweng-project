package ch.epfl.sweng.jassatepfl.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PlayerListAdapter extends ArrayAdapter<Player> {

    private final List<Player> players;
    private Match match;
    private final Set<String> selectedIds;

    public PlayerListAdapter(Context context, int resource, List<Player> players, Match match, Set<String> selectedIds) {
        super(context, resource, players);
        this.players = new ArrayList<>(players);
        this.match = match;
        this.selectedIds = new HashSet<>(selectedIds);
    }

    public PlayerListAdapter(Context context, int resource, List<Player> players) {
        this(context, resource, players, null, new HashSet<String>());
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
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    match == null ? R.layout.player_list_element : R.layout.player_grid_element,
                    parent, false);
        }

        Player p = getItem(position);

        TextView quoteTv = (TextView) convertView.findViewById(R.id.player_quote);
        quoteTv.setText(String.format(getContext().getString(R.string.player_list_adapter_quote), p.getQuote()));

        TextView playerName = (TextView) convertView.findViewById(R.id.player_name);
        if (match == null) {
            playerName.setText(p.toString());
        } else {
            String firstFirstName = p.getFirstName().split(" ")[0];
            playerName.setText(firstFirstName);
            String teamAssignment = match.teamNbForPlayer(p) == -1 ? "No Team" :
                    "Team " + (match.teamNbForPlayer(p) + 1);
            TextView teamView = (TextView) convertView.findViewById(R.id.player_team);
            teamView.setText(teamAssignment);
        }

        ImageView check = (ImageView) convertView.findViewById(R.id.player_check);
        check.setVisibility(selectedIds.contains(p.getID().toString()) ? VISIBLE : INVISIBLE);

        return convertView;
    }

    public void refreshData(List<Player> players, Match match, Set<String> selectedIds) {
        this.players.clear();
        this.players.addAll(players);
        this.match = match;
        this.selectedIds.clear();
        this.selectedIds.addAll(selectedIds);
        notifyDataSetChanged();
    }

    public void refreshData(List<Player> p) {
        this.players.clear();
        this.players.addAll(p);
        notifyDataSetChanged();
    }

    public void refreshData(Set<String> selectedIds) {
        this.selectedIds.addAll(selectedIds);
        notifyDataSetChanged();
    }

}
