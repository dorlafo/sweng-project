package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public final class MainActivity extends BaseActivityWithNavDrawer {
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fAuth.getCurrentUser() == null) {
            //Log.d(TAG, "showLogin:getCurrentUser:null");
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        } else {
            //Log.d(TAG, "showLogin:getCurrentUser:notNull");
            this.getSupportActionBar().setTitle("My Matches");
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_main, drawer, false);
            drawer.addView(contentView, 0);

            // Create the adapter that will return a fragment for each section
            mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                private final Fragment[] mFragments = new Fragment[] {
                        new PendingMatchListFragment(),
                        new ActiveMatchListFragment(),
                        new FinishedMatchListFragment(),
                };
                private final String[] mFragmentNames = new String[] {
                        getString(R.string.heading_pending_matches),
                        getString(R.string.heading_active_matches),
                        getString(R.string.heading_finished_matches)
                };
                @Override
                public Fragment getItem(int position) {
                    return mFragments[position];
                }
                @Override
                public int getCount() {
                    return mFragments.length;
                }
                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentNames[position];
                }
            };

            mViewPager = (ViewPager) findViewById(R.id.container_main_activity);
            mViewPager.setAdapter(mPagerAdapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_main_activity);
            tabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent startIntent = getIntent();

        // Notification onClick handler.
        // Can not display match name because it doesn't exists anymore.
        if (startIntent.hasExtra("notif") && startIntent.getStringExtra("notif").equals("matchexpired")) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notification_match_expired)
                    .show();
            startIntent.removeExtra("notif");
            startIntent.removeExtra("match_Id");
        }
    }
}
