package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

public final class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        //DO NOTHING -> it will disable the back button
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent startIntent = getIntent();

        // Notification onClick handler.
        // Can not display match name because it doesn't exists anymore.
        if(startIntent.hasExtra("notif") && startIntent.getStringExtra("notif").equals("matchexpired")) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.match_expired)
                    .show();
            startIntent.removeExtra("notif");
            startIntent.removeExtra("matchId");
        }
    }

    public void createMatch(View view) {
        Intent intent = new Intent(this, CreateMatchActivity.class);
        startActivity(intent);
    }

    /**
     * Launch the UserProfileActivity then the Profile button is clicked
     *
     * @param view Required param
     */
    public void viewProfile(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void displayMatchesOnMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void displayMatchesInList(View view) {
        Intent intent = new Intent(this, MatchListActivity.class);
        startActivity(intent);
    }
}
