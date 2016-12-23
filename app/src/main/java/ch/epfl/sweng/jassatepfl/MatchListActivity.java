package ch.epfl.sweng.jassatepfl;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import ch.epfl.sweng.jassatepfl.tools.MatchListAdapter;

import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.Gravity.CENTER_VERTICAL;

/**
 * Activity displaying matches as a scrolling list.
 * <br>
 * Clicking on a list item prompts the user to join the match.
 */
public class MatchListActivity extends BaseActivityWithNavDrawer implements OnItemClickListener {

    private MatchListAdapter adapter;
    private List<Match> matches;
    private ListView listView;
    private ChildEventListener childEventListener;

    private static final String TAG = MatchListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        } else {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_list, drawer, false);
            drawer.addView(contentView, 0);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            TextView emptyList = new TextView(this);
            emptyList.setGravity(CENTER_HORIZONTAL | CENTER_VERTICAL);
            emptyList.setText(R.string.list_empty_list);
            emptyList.setTextColor(Color.BLACK);
            emptyList.setLayoutParams(lp);

            listView = (ListView) findViewById(R.id.list_nearby_matches);
            ((ViewGroup) listView.getParent()).addView(emptyList);
            listView.setEmptyView(emptyList);

            matches = new ArrayList<>();
            listView.setOnItemClickListener(this);

            adapter = new MatchListAdapter(MatchListActivity.this, R.layout.match_list_row, new ArrayList<Match>());
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        contactFirebase();
    }

    @Override
    public void onPause() {
        super.onResume();
        if (childEventListener != null) {
            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES)
                    .orderByChild("privateMatch")
                    .equalTo(false)
                    .removeEventListener(childEventListener);
        }
        matches.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        // Opens dialog box to ask user if he wants to join match
        // Allows user to cancel or accept
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_join_match)
                .setMessage(R.string.dialog_join_message)
                .setPositiveButton(R.string.dialog_join_confirmation, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final Match match = adapter.getItem(position);
                        DatabaseUtils.addPlayerToMatch(MatchListActivity.this,
                                dbRefWrapped,
                                match.getMatchID(),
                                fAuth.getCurrentUser().getDisplayName(),
                                match);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, goes back to MatchListActivity
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (childEventListener != null) {
            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES)
                    .orderByChild("privateMatch")
                    .equalTo(false)
                    .removeEventListener(childEventListener);
        }
    }

    private void contactFirebase() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Match match = dataSnapshot.getValue(Match.class);
                //Add match to the list if we are not in it
                if (!match.hasParticipantWithID(new Player.PlayerID(getUserSciper()))) {
                    matches.add(match);
                }
                modifyListAdapter();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Match match = dataSnapshot.getValue(Match.class);
                int matchIndex = matches.indexOf(match);
                //If the match is in the list (ie we were not in it)
                if (matchIndex != -1) {
                    //if we now are in it, remove it from the list, otherwise modify it
                    if (match.hasParticipantWithID(new Player.PlayerID(getUserSciper()))) {
                        matches.remove(match);
                    } else {
                        matches.set(matchIndex, match);
                    }
                }
                //The match was not in the list
                else {
                    //Add match if we are not in it
                    if (!match.hasParticipantWithID(new Player.PlayerID(getUserSciper()))) {
                        matches.add(match);
                    }
                }
                modifyListAdapter();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
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
                .orderByChild("privateMatch").equalTo(false)
                .addChildEventListener(childEventListener);
    }

    /**
     * Updates Match list adapter
     */
    private void modifyListAdapter() {
        adapter.refreshData(matches);
    }

}
