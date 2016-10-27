package ch.epfl.sweng.project;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.tools.MatchListAdapter;

/**
 * Activity displaying matches as a scrolling list.
 *
 * @author Nicolas Phan Van
 */
public class MatchListActivity extends ListActivity {
    MatchListAdapter mAdapter;
    String sciper;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));

        // TODO: fix empty list
        mAdapter = new MatchListAdapter(this);

        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        // Opens dialog box to alert user of the authentication fail
        // Allows user to cancel or retry
        new AlertDialog.Builder(this)
                .setTitle(R.string.join_match)
                .setMessage(R.string.join_message)
                .setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        final Match match = mAdapter.getItem(position);
                        final String matchID = match.getMatchID();
                        sciper = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                        FirebaseDatabase.getInstance().getReference()
                                .child("players")
                                .child(sciper)
                                .addListenerForSingleValueEvent(new ValueEventListener(){

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        player = dataSnapshot.getValue(Player.class);
                                        match.addPlayer(player);
                                        ref.child(matchID).setValue(match);
                                        Intent moveToMatchActivity = new Intent(MatchListActivity.this, MatchActivity.class);
                                        getIntent().putExtra("MATCH_ID", matchID);
                                        startActivity(moveToMatchActivity);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, goes back to ListMatchActivity
                    }
                })
                .show();
        super.onListItemClick(l, v, position, id);
    }

    public void switchToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
