package ch.epfl.sweng.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import ch.epfl.sweng.project.database.MatchProvider;
import ch.epfl.sweng.project.model.Match;

public class CreateMatchActivity extends AppCompatActivity
        implements OnClickListener {
    private MatchProvider mProvider;
    private Match.Builder matchBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        mProvider = new MatchProvider();
        matchBuilder = new Match.Builder();

        // getId of all buttons
        Button createMatch = (Button) findViewById(R.id.create_create_button);
        createMatch.setOnClickListener(this);

        Button confirmDescription = (Button) findViewById(R.id.description_confirmation);
        confirmDescription.setOnClickListener(this);

        Switch privacySwitch = (Switch) findViewById(R.id.switch_private);
        privacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                matchBuilder.setPrivacy(isChecked);
            }
        });

        Spinner playerNumSpinner = (Spinner) findViewById(R.id.player_num_spinner);
        Spinner variantSpinner = (Spinner) findViewById(R.id.variant_spinner);
        ArrayAdapter<CharSequence> playerNumAdapter = ArrayAdapter.createFromResource(this,
                R.array.player_num_array, android.R.layout.simple_spinner_item);
        //ArrayAdapter<CharSequence> variantAdapter = ArrayAdapter.createFromResource(this,Match.GameVariant.getVariantName(), android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> variantAdapter = ArrayAdapter.createFromResource(this,
                R.array.variant_array, android.R.layout.simple_spinner_item);

        playerNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerNumSpinner.setAdapter(playerNumAdapter);
        variantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        variantSpinner.setAdapter(variantAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_create_button:
                publishMatch(matchBuilder.build());
                break;
            case R.id.description_confirmation:
                EditText editDescription = (EditText) findViewById(R.id.description_match_text);
                matchBuilder.setDescription(editDescription.getText().toString());
                break;
            default:
                break;
        }
    }

    void publishMatch(Match match) {
        mProvider.writeNewMatchToDB(match);
    }

}
