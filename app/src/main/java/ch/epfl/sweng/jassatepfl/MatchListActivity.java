package ch.epfl.sweng.jassatepfl;


import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import ch.epfl.sweng.jassatepfl.tools.MatchListAdapter;

/**
 * Activity displaying matches as a scrolling list.
 */
public class MatchListActivity extends ListActivity {

    private MatchListAdapter mAdapter;

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
        // Opens dialog box to ask user if he wants to join match
        // Allows user to cancel or accept
        new AlertDialog.Builder(this)
                .setTitle(R.string.join_match)
                .setMessage(R.string.join_message)
                .setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final Match match = mAdapter.getItem(position);
                        DatabaseUtils.addPlayerToMatch(MatchListActivity.this,
                                FirebaseDatabase.getInstance().getReference(),
                                match.getMatchID(),
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                match);
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
