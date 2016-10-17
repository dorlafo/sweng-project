package ch.epfl.sweng.project.database;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.model.Match;

public class MatchProvider {
    private final Map<String, Match> matches;
    private final DatabaseReference dRef;
    private final ChildEventListener matchListener;

    public MatchProvider() {
        matches = new HashMap<>();
        dRef = FirebaseDatabase.getInstance().getReference("matches");
        matchListener = new MatchEventListener();
        dRef.addChildEventListener(matchListener);
    }

    public void close() {
        dRef.removeEventListener(matchListener);
    }

    public Map<String, Match> provide() {
        return matches;
    }

    public void writeMatchToDB(Match matchToWrite) {
        DatabaseReference whereToWriteRef = dRef.getRoot().child("matches");
        whereToWriteRef.push().setValue(matchToWrite);
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
