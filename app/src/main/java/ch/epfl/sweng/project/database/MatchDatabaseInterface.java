package ch.epfl.sweng.project.database;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;

import java.security.ProviderException;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.project.model.Match;

/**
 * MatchDatabaseInterface is a class that provides operation to interact with the Firebase server.
 */
public class MatchDatabaseInterface {
    private final Map<String, Match> matches;
    private final DatabaseReference dRef;
    private final ChildEventListener matchListener;
    private final DatabaseReference.CompletionListener completionListener;

    public MatchDatabaseInterface() {
        matches = new HashMap<>();
        dRef = FirebaseDatabase.getInstance().getReference("matches");
        matchListener = new MatchEventListener();
        dRef.addChildEventListener(matchListener);
        completionListener = new MatchCompletionListener();
    }

    public void close() {
        dRef.removeEventListener(matchListener);
    }

    public Map<String, Match> provide() {
        return matches;
    }

    public String writeNewMatch(Match matchToWrite) {
        dRef.push().setValue(matchToWrite, completionListener);
        String token = dRef.getKey();
        return token;
    }

    public void deleteMatch(String token) {
        dRef.getRoot().child(token).removeValue(completionListener);
    }

    public void updateMatch(String id, Match match) {
        dRef.child(id).setValue(match, completionListener);
    }

    public class MatchCompletionListener implements CompletionListener {


        @Override
        public void onComplete(DatabaseError error, DatabaseReference ref) {
            if(error != null) {
                throw new ProviderException("Firebase operation did not complete");

            }
        }

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
