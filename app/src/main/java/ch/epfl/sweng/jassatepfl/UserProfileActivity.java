package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.jassatepfl.model.Player;

public class UserProfileActivity extends BaseActivityWithNavDrawer {

    private final String TAG = UserProfileActivity.class.getSimpleName();
    private TextView mtwPlayerID;
    private TextView mtwLastName;
    private TextView mtwFirstName;
    private TextView mtwPlayerRank;
    String sciper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sciper = fAuth.getCurrentUser().getDisplayName();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_user_profile, null, false);
        drawer.addView(contentView, 0);

        mtwPlayerID = (TextView) findViewById(R.id.twPlayerID);
        mtwLastName = (TextView) findViewById(R.id.twLastName);
        mtwFirstName = (TextView) findViewById(R.id.twFirstName);
        mtwPlayerRank = (TextView) findViewById(R.id.twRank);

        //New ChildEventListener that will change the value of the textView according to the current
        //logged in user

        dbRefWrapped
                .child("players")
                .child(sciper)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Player p = dataSnapshot.getValue(Player.class);
                        mtwPlayerID.setText(mtwPlayerID.getText() + " " + p.getID().toString());
                        System.out.println(p.getID().toString());
                        mtwLastName.setText(mtwLastName.getText() + " " + p.getLastName());
                        mtwFirstName.setText(mtwFirstName.getText() + " " + p.getFirstName());
                        mtwPlayerRank.setText(mtwPlayerRank.getText() + " " + p.getRank().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void viewMenu(View view) {
        finish();
    }

}
