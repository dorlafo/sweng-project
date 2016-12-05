package ch.epfl.sweng.jassatepfl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class TimeSeriesFragment extends StatsFragment {
    public TimeSeriesFragment() {
        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_series, container, false);

        ImageView variants = (ImageView) rootView.findViewById(R.id.played_graph);
        retrieveAndDisplay(variants, "played");

        ImageView partners = (ImageView) rootView.findViewById(R.id.won_graph);
        retrieveAndDisplay(partners, "won");

        ImageView wonWith = (ImageView) rootView.findViewById(R.id.rank_graph);
        retrieveAndDisplay(wonWith, "rank");

        return rootView;
    }


}
