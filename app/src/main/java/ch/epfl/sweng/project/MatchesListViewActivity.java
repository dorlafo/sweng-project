package ch.epfl.sweng.project;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.res.DummyMatchData;
import ch.epfl.sweng.project.tools.CustomAdapter;

public class MatchesListViewActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = (ListView) findViewById(android.R.id.list);

        // TODO: fix empty list and filter private matches

        BaseAdapter customAdapter = new CustomAdapter(this, DummyMatchData.dummyMatches());
        //BaseAdapter customAdapter = new CustomAdapter(this, new ArrayList<Match>());

        listView.setAdapter(customAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void switchToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
