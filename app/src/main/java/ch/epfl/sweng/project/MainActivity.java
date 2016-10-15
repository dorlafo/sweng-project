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
    public static Map<String, Match> matches;
    private DatabaseReference dRef;
    private ChildEventListener matchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrieveMatchList();
    }

    public void createMatch(View view) {
        Intent intent = new Intent(this, CreateMatchActivity.class);
        startActivity(intent);
    }

    private void retrieveMatchList() {
        matches = new HashMap<>();
        dRef = FirebaseDatabase.getInstance().getReference("matches");
        matchListener = new MatchEventListener();
        dRef.addChildEventListener(matchListener);
    }

    @Override
    protected void onStop() {
        dRef.removeEventListener(matchListener);
        super.onStop();
    }

    private class MatchEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Match newMatch = dataSnapshot.getValue(Match.class);
            matches.put(dataSnapshot.getKey(), newMatch);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Match newMatch = dataSnapshot.getValue(Match.class);
            matches.put(dataSnapshot.getKey(), newMatch);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            matches.remove(dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) { }
    }
}