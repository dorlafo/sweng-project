package ch.epfl.sweng.jassatepfl;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.injections.Graph;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.stats.DumbGenerator;
import ch.epfl.sweng.jassatepfl.stats.StatsUpdate;
import ch.epfl.sweng.jassatepfl.stats.UserStats;

public class StatsActivity extends BaseAppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    UserStats data = DumbGenerator.getStuff();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        String sciper = fAuth.getCurrentUser().getDisplayName();

        dbRefWrapped.child("stats").child("user").child(sciper).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue(UserStats.class);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    /**
     * Fragment describing the evolution tab, showing the evolution of user stats in time.
     */
    public static class EvolutionFragment extends Fragment {
        public EvolutionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.stats_counter_fragment, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.won_field);
            //textView.setText(getString(R.stri0w0ng.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            /*
            GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0, 1),
                    new DataPoint(1, 2),
                    new DataPoint(2, 3),
                    new DataPoint(3, 4),
                    new DataPoint(4, 5)
            });
            graph.addSeries(series);
            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setScalableY(true);
            graph.getViewport().setScrollableY(true);
            */

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new CountersFragment();
                case 1:
                   return new TimeSeriesFragment();
                default:
                    return new ScoreboardFragment().setReference(dbRefWrapped);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Counters";
                case 1:
                    return "Evolution";
                case 2:
                    return "Scoreboard";
            }
            return null;
        }
    }
}
