package ch.epfl.sweng.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sweng.project.database.MatchProvider;
//TODO Remove when tests are done


/**
 * Your app's main activity.
 */
public final class MainActivity extends AppCompatActivity {
    private MatchProvider mProvider;
    //TODO convention de nommage pour les providers ? "m" pour match puis "Provider". A décider.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProvider = new MatchProvider();
    }

    public void createMatch(View view) {
        Intent intent = new Intent(this, CreateMatchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        mProvider.close();
        super.onStop();
    }
}