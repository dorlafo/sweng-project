package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mukesh.MarkdownView;

import ch.epfl.sweng.jassatepfl.model.Match;

/**
 * @author Alexis Montavon
 *
 * Activity class allowing user to read the rules of the game
 * for all variants.
 */
public class RulesActivity extends BaseActivityWithNavDrawer {
    private MarkdownView userGuideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rules);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_rules, drawer, false);
        drawer.addView(contentView, 0);
        userGuideView = (MarkdownView) findViewById(R.id.rules_text);

        ArrayAdapter<Match.GameVariant> variantAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Match.GameVariant.values());
        variantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner variantSpinner = (Spinner) findViewById(R.id.rules_spinner);
        variantSpinner.setAdapter(variantAdapter);
        variantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if(parent.getId() == R.id.rules_spinner) {
                    switch((Match.GameVariant)item) {
                        case CHIBRE:
                            userGuideView.loadMarkdownFromAssets("ChibreRules.md");
                            break;
                        case PIQUE_DOUBLE:
                            userGuideView.loadMarkdownFromAssets("PiqueDoubleRules.md");
                            break;
                        case OBEN_ABE:
                            userGuideView.loadMarkdownFromAssets("ObenAbeRules.md");
                            break;
                        case UNDEN_UFE:
                            userGuideView.loadMarkdownFromAssets("UndenUfeRules.md");
                            break;
                        case SLALOM:
                            userGuideView.loadMarkdownFromAssets("SlalomRules.md");
                            break;
                        case CHICANE:
                            userGuideView.loadMarkdownFromAssets("ChicaneRules.md");
                            break;
                        case JASS_MARANT:
                            userGuideView.loadMarkdownFromAssets("JassMarantRules.md");
                            break;
                        case ROI:
                            userGuideView.loadMarkdownFromAssets("RoiRules.md");
                            break;
                        case POMME:
                            userGuideView.loadMarkdownFromAssets("PommeRules.md");
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userGuideView.loadMarkdownFromAssets("ChibreRules.md");
            }
        });
    }
}
