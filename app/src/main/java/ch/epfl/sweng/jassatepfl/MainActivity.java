package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public final class MainActivity extends BaseActivityWithNavDrawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, drawer, false);
        drawer.addView(contentView, 0);

        TextView emptyList = new TextView(this);
        emptyList.setText(R.string.main_empty_list);
        emptyList.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        emptyList.setTextColor(Color.BLACK);

        ListView listView = (ListView) findViewById(android.R.id.list);
        ((ViewGroup) listView.getParent()).addView(emptyList);
        listView.setEmptyView(emptyList);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent startIntent = getIntent();

        // Notification onClick handler.
        // Can not display match name because it doesn't exists anymore.
        if (startIntent.hasExtra("notif") && startIntent.getStringExtra("notif").equals("matchexpired")) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notification_match_expired)
                    .show();
            startIntent.removeExtra("notif");
            startIntent.removeExtra("matchId");
        }
    }

    public void createMatch(View view) {
        Intent intent = new Intent(this, CreateMatchActivity.class);
        startActivity(intent);
    }

    public void showEnrolledMatch(View view){
        Intent intent = new Intent(this, CreateMatchActivity.class);
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
