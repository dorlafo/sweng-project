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
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

public class UserProfileActivity extends BaseActivityWithNavDrawer {

    private TextView mtwPlayer;
    private TextView mtwPlayerQuote;
    String sciper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_user_profile, drawer, false);
            drawer.addView(contentView, 0);

            mtwPlayer = (TextView) findViewById(R.id.profil_player);
            mtwPlayerQuote = (TextView) findViewById(R.id.twQuote);

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
                            mtwPlayer.setText(p.getFirstName() + " " + p.getLastName());
                            mtwPlayerQuote.setText(mtwPlayerQuote.getText() + " " + Integer.toString(p.getQuote()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Nothing to be done
                        }
                    });
        }
    }

    //public void viewMenu(View view) {
    //    finish();
    //}

}
