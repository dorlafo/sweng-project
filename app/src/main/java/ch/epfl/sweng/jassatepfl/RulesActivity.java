package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mukesh.MarkdownView;

import ch.epfl.sweng.jassatepfl.model.Match;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Activity class allowing user to read the rules of the game
 * for all variants.
 *
 * @author Alexis Montavon
 */
public class RulesActivity extends BaseActivityWithNavDrawer {

    private MarkdownView userGuideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_rules, drawer, false);
        drawer.addView(contentView, 0);
        userGuideView = (MarkdownView) findViewById(R.id.rules_text);

        View toolbarLayout = inflater.inflate(R.layout.rules_toolbar_layout, drawer, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.addView(toolbarLayout);

        ArrayAdapter<Match.GameVariant> variantAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Match.GameVariant.values());
        variantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner variantSpinner = (Spinner) findViewById(R.id.rules_spinner);
        variantSpinner.setAdapter(variantAdapter);
        variantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = ((TextView) parent.getChildAt(0));
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(COMPLEX_UNIT_SP, 20f);
                Object item = parent.getItemAtPosition(position);
                switch ((Match.GameVariant) item) {
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userGuideView.loadMarkdownFromAssets("ChibreRules.md");
            }
        });
    }

}
