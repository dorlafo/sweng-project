package ch.epfl.sweng.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.database.MatchProvider;

public class CreateMatchActivity extends AppCompatActivity
        implements View.OnClickListener {
    MatchProvider mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);
        mProvider = new MatchProvider();

        // getId of all buttons
        Button createMatch = (Button) findViewById(R.id.create_create_button);
        createMatch.setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.player_num_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.player_num_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_create_button:
                //publishMatch();
                break;
            default:
                break;
        }
    }

    void publishMatch(Match match) {
        mProvider.writeNewMatchToDB(match);
    }

}
