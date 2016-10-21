package ch.epfl.sweng.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import ch.epfl.sweng.project.database.UserProvider;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.model.Rank;

public class UserProfileActivity extends AppCompatActivity {

    private UserProvider up;
    private ChildEventListener playerListener;
    private TextView mtwPlayerID;
    private TextView mtwLastName;
    private TextView mtwFirstName;
    private TextView mtwPlayerRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Player p = dataSnapshot.getValue(Player.class);
                //players.put(dataSnapshot.getKey(), p);
                mtwPlayerID.setText(mtwPlayerID.getText() + " " + p.getID().toString());
                mtwLastName.setText(mtwLastName.getText() + " " + p.getLastName());
                mtwFirstName.setText(mtwFirstName.getText() + " " + p.getFirstName());
                mtwPlayerRank.setText(mtwPlayerRank.getText() + " " + p.getRank().toString());
                System.out.println("PLAYER-EVENT-LISTENER----: ON-CHILD-ADDED");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Player p = dataSnapshot.getValue(Player.class);
                //players.put(dataSnapshot.getKey(), p);
                System.out.println("PLAYER-EVENT-LISTENER----: ON-CHILD-cCHANGED");
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
        up = new UserProvider(playerListener);
        setContentView(R.layout.activity_user_profile);
    }

    @Override
    public void onStart() {
        super.onStart();

        mtwPlayerID = (TextView) findViewById(R.id.twPlayerID);
        mtwLastName = (TextView) findViewById(R.id.twLastName);
        mtwFirstName = (TextView) findViewById(R.id.twFirstName);
        mtwPlayerRank = (TextView) findViewById(R.id.twRank);


        //Dummy data for demo purpose
/*
        //up.addUser(new Player(new Player.PlayerID(234832), "Laforest", "Dorian", new Rank(42)));
        Player p = null;
        //while(p == null) {
        p = up.getPlayerWithID(new Player.PlayerID(234832));
        //}
        if(p != null) {
            mtwPlayerID.setText(mtwPlayerID.getText() + " " + p.getID().toString());
            mtwLastName.setText(mtwLastName.getText() + " " + p.getLastName());
            mtwFirstName.setText(mtwFirstName.getText() + " " + p.getFirstName());
            mtwPlayerRank.setText(mtwPlayerRank.getText() + " " + p.getRank().toString());
        }
*/
    }

    public void viewMenu(View view) {
        finish();
    }
}
