package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import ch.epfl.sweng.jassatepfl.tools.EnrolledMatchListAdapter;

import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.Gravity.CENTER_VERTICAL;

public final class MainActivity extends BaseActivityWithNavDrawer implements AdapterView.OnItemClickListener {

    private BaseAdapter adapter;
    private ListView listView;
    private List<Match> matches;
    private ChildEventListener childEventListener;

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

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            TextView emptyList = new TextView(this);
            emptyList.setGravity(CENTER_HORIZONTAL | CENTER_VERTICAL);
            emptyList.setText(R.string.main_empty_list);
            emptyList.setTextColor(Color.BLACK);
            emptyList.setLayoutParams(lp);

            listView = (ListView) findViewById(R.id.list_my_matches);
            ((ViewGroup) listView.getParent()).addView(emptyList);
            listView.setEmptyView(emptyList);

            matches = new ArrayList<>();
            listView.setOnItemClickListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        contactFirebase();
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

    @Override
    public void onPause() {
        super.onPause();
        if (childEventListener != null) {
            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES).removeEventListener(childEventListener);
        }
        matches.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (childEventListener != null) {
            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES).removeEventListener(childEventListener);
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Match match = (Match) adapter.getItem(position);
        if (match.getMatchStatus().equals(Match.MatchStatus.ACTIVE)) {
            Intent goToGameActivity = new Intent(this, GameActivity.class);
            goToGameActivity.putExtra("match_Id", match.getMatchID());
            goToGameActivity.putExtra("mode", "online");
            startActivity(goToGameActivity);
        } else {
            Intent moveToWaitingPlayersActivity = new Intent(this, WaitingPlayersActivity.class);
            moveToWaitingPlayersActivity.putExtra("match_Id", match.getMatchID());
            startActivity(moveToWaitingPlayersActivity);
        }
    }

    private void contactFirebase() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Log.d(TAG, "onChildAdded:dataSnapshot:" + dataSnapshot.toString());
                Match match = dataSnapshot.getValue(Match.class);
                //Add match to the list if we are in it
                if (match.hasParticipantWithID(new Player.PlayerID(getUserSciper()))) {
                    matches.add(match);
                }
                modifyListAdapter();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Log.d(TAG, "onChildChanged:dataSnapshot:" + dataSnapshot.toString());
                Match match = dataSnapshot.getValue(Match.class);
                int matchIndex = matches.indexOf(match);
                //If the match is in the list (ie we were in it)
                if (matchIndex != -1) {
                    //If we now are not in it, remove it from the list, otherwise modify it
                    if (!match.hasParticipantWithID(new Player.PlayerID(getUserSciper()))) {
                        matches.remove(match);
                    } else {
                        matches.set(matchIndex, match);
                    }
                }
                //The match was not in the list
                else {
                    //Add match if we are in it
                    if (match.hasParticipantWithID(new Player.PlayerID(getUserSciper()))) {
                        matches.add(match);
                    }
                }
                modifyListAdapter();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Log.d(TAG, "onChildRemoved:dataSnapshot:" + dataSnapshot.toString());
                Match match = dataSnapshot.getValue(Match.class);
                matches.remove(match);
                modifyListAdapter();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES)
                .addChildEventListener(childEventListener);
    }

    /**
     * Updates Match list adapter
     */
    private void modifyListAdapter() {
        adapter = new EnrolledMatchListAdapter(MainActivity.this, R.layout.match_enrolled_list_row, matches);
        listView.setAdapter(adapter);
    }

    public void viewStats(View view) {
        startActivity(new Intent(this, StatsActivity.class));
    }
}
