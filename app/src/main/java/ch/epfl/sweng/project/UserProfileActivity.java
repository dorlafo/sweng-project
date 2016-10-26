package ch.epfl.sweng.project;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.project.database.UserProvider;
import ch.epfl.sweng.project.model.Player;

public class UserProfileActivity extends AppCompatActivity {

    private final String TAG = UserProfileActivity.class.getSimpleName();
    private TextView mtwPlayerID;
    private TextView mtwLastName;
    private TextView mtwFirstName;
    private TextView mtwPlayerRank;
    String sciper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sciper = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        //New ChildEventListener that will change the value of the textView according to the current
        //logged in user
        FirebaseDatabase.getInstance().getReference()
                .child("players")
                .child(sciper)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Player p = dataSnapshot.getValue(Player.class);
                        mtwPlayerID.setText(mtwPlayerID.getText() + " " + p.getID().toString());
                        mtwLastName.setText(mtwLastName.getText() + " " + p.getLastName());
                        mtwFirstName.setText(mtwFirstName.getText() + " " + p.getFirstName());
                        mtwPlayerRank.setText(mtwPlayerRank.getText() + " " + p.getRank().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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

    public void viewMenu(View view) {
        finish();
    }
}
