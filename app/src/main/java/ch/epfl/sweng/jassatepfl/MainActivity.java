package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.tools.MatchListEnrolledAdapter;

public final class MainActivity extends BaseActivityWithNavDrawer  implements AdapterView.OnItemClickListener {

    private BaseAdapter adapter;
    private ListView listView;
    private List<Match> matches;
    private ChildEventListener childEventListener;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fAuth.getCurrentUser() == null) {
            Log.d(TAG, "showLogin:getCurrentUser:null");
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            Log.d(TAG, "showLogin:getCurrentUser:NOTnull");
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_main, drawer, false);
            drawer.addView(contentView, 0);

            TextView emptyList = new TextView(this);
            emptyList.setText(R.string.main_empty_list);
            emptyList.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            emptyList.setTextColor(Color.BLACK);

            listView = (ListView) findViewById(android.R.id.list);
            ((ViewGroup) listView.getParent()).addView(emptyList);
            listView.setEmptyView(emptyList);

            matches = new ArrayList<>();
            contactFirebase();
            listView.setOnItemClickListener(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(childEventListener != null) {
            dbRefWrapped.removeEventListener(childEventListener);
        }
    }

    public void createMatch(View view) {
        Intent intent = new Intent(this, CreateMatchActivity.class);
        startActivity(intent);
    }

    public void displayMatchesOnMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void displayMatchesInList(View view) {
        Intent intent = new Intent(this, MatchListActivity.class);
        startActivity(intent);
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
        //TODO: Make new intent with extras and move to GameActivity
        if(match.getMatchStatus().equals(Match.MatchStatus.ACTIVE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Feature missing")
                    .setMessage("will move to GameActivity")
                    .show();
        }
        else {
            Intent moveToMatchActivity = new Intent(this, WaitingPlayersActivity.class);
            moveToMatchActivity.putExtra("match_Id", match.getMatchID());
            startActivity(moveToMatchActivity);
        }
    }

    private void contactFirebase() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded:dataSnapshot:" + dataSnapshot.toString());
                Match match = dataSnapshot.getValue(Match.class);
                matches.add(match);
                modifyListAdapter();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged:dataSnapshot:" + dataSnapshot.toString());
                Match match = dataSnapshot.getValue(Match.class);
                int matchIndex = matches.indexOf(match);
                if(matchIndex != -1) {
                    matches.set(matchIndex, match);
                }
                modifyListAdapter();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:dataSnapshot:" + dataSnapshot.toString());
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
        dbRefWrapped.child("matchesByPlayer")
                .child(getUserSciper())
                .addChildEventListener(childEventListener);
    }

    /**
     * Updates Match list adapter
     */
    private void modifyListAdapter() {
        adapter = new MatchListEnrolledAdapter(MainActivity.this, R.layout.match_enrolled_list_row, matches);
        listView.setAdapter(adapter);
    }
}
