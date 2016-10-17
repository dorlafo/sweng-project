package ch.epfl.sweng.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.database.MatchProvider;

public class CreateMatchActivity extends AppCompatActivity {
    MatchProvider mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mProvider = new MatchProvider();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);
        Spinner spinner = (Spinner) findViewById(R.id.player_num_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.player_num_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    void publishMatch(Match m) {
        mProvider.writeNewMatchToDB(m);
    }

}
