package ch.epfl.sweng.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.project.model.Match;

/**
 * Your app's main activity.
 */
public final class MainActivity extends AppCompatActivity {
    Map<String, Match> matches;
    DatabaseReference dRef;
    ChildEventListener matchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        matches = new HashMap<>();
        dRef = FirebaseDatabase.getInstance().getReference("matches");
        matchListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Match newMatch = dataSnapshot.getValue(Match.class);
                matches.put(dataSnapshot.getKey(), newMatch);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                matches.put(dataSnapshot.getKey(), dataSnapshot.getValue(Match.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                matches.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dRef.addChildEventListener(matchListener);
    }

    public void createMatch(View view) {
        Intent intent = new Intent(this, CreateMatchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        dRef.removeEventListener(matchListener);
        super.onStop();
    }
}