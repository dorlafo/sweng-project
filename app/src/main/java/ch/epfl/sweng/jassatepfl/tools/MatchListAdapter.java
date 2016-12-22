package ch.epfl.sweng.jassatepfl.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.model.Match;

public final class MatchListAdapter extends ArrayAdapter<Match> {

    private List<Match> matches;

    public MatchListAdapter(Context context, int resource, List<Match> matches) {
        super(context, resource, matches);
        this.matches = new ArrayList<>(matches);
    }

    @Override
    public int getCount() {
        return matches.size();
    }

    @Override
    public Match getItem(int position) {
        return matches.get(position);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.match_list_row, parent, false);
        }

        Match match = matches.get(position);
        MatchStringifier stringifier = new MatchStringifier(getContext());
        stringifier.setMatch(match);

        TextView description = (TextView) convertView.findViewById(R.id.description);
        description.setText(match.getDescription());

        TextView quoteData = (TextView) convertView.findViewById(R.id.quote_data);
        quoteData.setText(stringifier.quoteToString());

        TextView players = (TextView) convertView.findViewById(R.id.players_data);
        //players.setText(stringifier.playersToString());
        players.setText(String.format(getContext().getString(R.string.match_list_adapter_players_nb), match.getPlayers().size(), match.getMaxPlayerNumber()));

        TextView variant = (TextView) convertView.findViewById(R.id.variant_data);
        variant.setText(stringifier.variantToString());

        TextView expirationDate = (TextView) convertView.findViewById(R.id.expiration_date_data);
        expirationDate.setText(stringifier.dateToStringCustom());

        TextView hasCard = (TextView) convertView.findViewById(R.id.has_card);
        if(match.hasCards()) {
            hasCard.setText(R.string.yes);
        } else {
            hasCard.setText(R.string.no);
        }
        return convertView;
    }

    public void refreshData(List<Match> m) {
        this.matches.clear();
        this.matches.addAll(m);
        notifyDataSetChanged();
    }
}
