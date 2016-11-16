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
import ch.epfl.sweng.jassatepfl.model.Player;

/**
 * Adapter for Player List
 *
 * @author Alexis Montavon
 */
public class PlayerListAdapter extends ArrayAdapter<Player> {

    public PlayerListAdapter(Context context, int resource, List<Player> items) {
        super(context, resource, items);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.player_list_element, null);
        }

        Player p = getItem(position);
        if (p != null) {
            TextView tv = (TextView) v.findViewById(R.id.player_name);
            if (tv != null) {
                tv.setText(p.toString());
            }
        }
        return v;
    }

}
