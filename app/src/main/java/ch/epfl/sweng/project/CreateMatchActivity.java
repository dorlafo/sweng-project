package ch.epfl.sweng.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.database.MatchDatabaseInterface;

public class CreateMatchActivity extends AppCompatActivity {
    MatchDatabaseInterface matchDBInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        matchDBInterface = new MatchDatabaseInterface();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);
        Spinner spinner = (Spinner) findViewById(R.id.player_num_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.player_num_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    void publishMatch(Match m) {
        matchDBInterface.writeNewMatch(m);
    }

}
