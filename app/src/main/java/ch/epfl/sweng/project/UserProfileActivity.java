package ch.epfl.sweng.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;

import ch.epfl.sweng.project.database.UserProvider;
import ch.epfl.sweng.project.model.Player;

public class UserProfileActivity extends AppCompatActivity {

    private final String TAG = "UserProfileActivity";
    private UserProvider up;
    private ChildEventListener playerListener;
    private TextView mtwPlayerID;
    private TextView mtwLastName;
    private TextView mtwFirstName;
    private TextView mtwPlayerRank;
    private String mUserKey = getRandomKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        up = new UserProvider();

        playerListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(mUserKey)) {
                    Player p = dataSnapshot.getValue(Player.class);
                    mtwPlayerID.setText(mtwPlayerID.getText() + " " + p.getID().toString());
                    mtwLastName.setText(mtwLastName.getText() + " " + p.getLastName());
                    mtwFirstName.setText(mtwFirstName.getText() + " " + p.getFirstName());
                    mtwPlayerRank.setText(mtwPlayerRank.getText() + " " + p.getRank().toString());
                }
                Log.d(TAG, "PLAYER-EVENT-LISTENER----: ON-CHILD-ADDED");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(mUserKey)) {
                    Player p = dataSnapshot.getValue(Player.class);
                    mtwPlayerID.setText(mtwPlayerID.getText() + " " + p.getID().toString());
                    mtwLastName.setText(mtwLastName.getText() + " " + p.getLastName());
                    mtwFirstName.setText(mtwFirstName.getText() + " " + p.getFirstName());
                    mtwPlayerRank.setText(mtwPlayerRank.getText() + " " + p.getRank().toString());
                }
                Log.d(TAG, "PLAYER-EVENT-LISTENER----: ON-CHILD-CHANGED");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //players.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //Nothing to be done
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Nothing to be done
            }
        };

        up.addEventListener(playerListener);


        setContentView(R.layout.activity_user_profile);
    }

    @Override
    public void onStart() {
        super.onStart();

        mtwPlayerID = (TextView) findViewById(R.id.twPlayerID);
        mtwLastName = (TextView) findViewById(R.id.twLastName);
        mtwFirstName = (TextView) findViewById(R.id.twFirstName);
        mtwPlayerRank = (TextView) findViewById(R.id.twRank);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        up.close();
    }


    public void viewMenu(View view) {
        up.close();
        finish();
    }


    /**
     * Private method to get a random user profile id
     * This is use for demo ONLY, the real key will come from
     * the Tequila login
     * @return The random key chosen between the project member sciper
     */
    private String getRandomKey() {
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add("234832");
        keyList.add("249733");
        keyList.add("245433");
        keyList.add("239293");
        keyList.add("235400");
        keyList.add("226647");
        Collections.shuffle(keyList);
        Log.d(TAG, "Chosen key is " + keyList.get(0));
        return keyList.get(0);
    }
}
