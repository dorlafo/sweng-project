package ch.epfl.sweng.project;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import ch.epfl.sweng.project.tools.MatchListAdapter;

/**
 * Activity displaying matches as a scrolling list.
 *
 * @author Nicolas Phan Van
 */
public class MatchListActivity extends ListActivity {
    MatchListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));

        // TODO: fix empty list and filter private matches
        mAdapter = new MatchListAdapter(this);

        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
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
