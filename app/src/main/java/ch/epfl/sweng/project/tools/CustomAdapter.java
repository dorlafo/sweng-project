package ch.epfl.sweng.project.tools;

import android.content.Context;
import android.widget.BaseAdapter;

import ch.epfl.sweng.project.model.Match;

import com.google.common.collect.Iterables;


/**
 * Created by phanvan on 11/10/16.
 */

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private Iterable<Match> matches;

    public CustomAdapter(Context context, Iterable<Match> matches) {
        this.context = context;
        this.matches = matches;
    }

    @Override
    public int getCount() {
        return Iterables.size(matches);
    }
}
