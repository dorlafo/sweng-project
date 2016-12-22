package ch.epfl.sweng.jassatepfl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Fragment containing the "counter" statistics.
 */
public class CountersFragment extends StatsFragment {
    public CountersFragment() {
        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stats_counter_fragment, container, false);

        ImageView variants = (ImageView) rootView.findViewById(R.id.variants_graph);
        retrieveAndDisplay(variants, "variants");

        ImageView partners = (ImageView) rootView.findViewById(R.id.partners_graph);
        retrieveAndDisplay(partners, "partners");

        ImageView wonWith = (ImageView) rootView.findViewById(R.id.won_with_graph);
        retrieveAndDisplay(wonWith, "wonWith");

        return rootView;
    }
}

