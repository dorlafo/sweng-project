package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.stats.UserStats;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

public class UserProfileActivity extends BaseActivityWithNavDrawer {

    private TextView mtwPlayer;
    private TextView mtwPlayerQuote;
    private TextView mtwMatchPlayed;
    private TextView mtwMatchWon;
    private TextView mtwRecurentPartner;
    private TextView mtwBestPartner;
    private TextView mtwVariant;
    String sciper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        } else {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_user_profile, drawer, false);
            drawer.addView(contentView, 0);

            mtwPlayer = (TextView) findViewById(R.id.profil_player);
            mtwPlayerQuote = (TextView) findViewById(R.id.twQuote);
            mtwMatchPlayed = (TextView) findViewById(R.id.twMatchPlayed);
            mtwMatchWon = (TextView) findViewById(R.id.twMatchWon);
            mtwRecurentPartner = (TextView) findViewById(R.id.twMostPlayedWith);
            mtwBestPartner = (TextView) findViewById(R.id.twMostWonWith);
            mtwVariant = (TextView) findViewById(R.id.twMostVariant);

            sciper = getUserSciper();

            //New SingleEventListener that will change the value of the textView according to the current
            //logged in user
            dbRefWrapped
                    .child(DatabaseUtils.DATABASE_PLAYERS)
                    .child(sciper)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Player p = dataSnapshot.getValue(Player.class);
                            mtwPlayer.setText(String.format(getString(R.string.profile_label_name), p.getFirstName(), p.getLastName()));
                            mtwPlayerQuote.setText(String.format(getString(R.string.profile_label_quote), p.getQuote()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Nothing to be done
                        }
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        contactFirebase();
    }

    private void contactFirebase() {
        dbRefWrapped
                .child(DatabaseUtils.DATABASE_USERSTATS)
                .child(sciper)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserStats us = dataSnapshot.getValue(UserStats.class);
                        mtwMatchPlayed.setText(String.format(getString(R.string.profile_label_matches_played), us.getPlayedMatches()));
                        mtwMatchWon.setText(String.format(getString(R.string.profile_label_matches_won), us.getWonMatches()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
