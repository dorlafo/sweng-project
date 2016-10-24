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
 * MatchDatabaseInterface is a class that provides operations to interact with the Firebase server.
 */
public class MatchDatabaseInterface {
    private final Map<String, Match> matches;
    private final DatabaseReference dRef;
    private final ChildEventListener matchListener;
    private final DatabaseReference.CompletionListener completionListener;

    /**
     * Constructor of the MatchDatabaseInterface.
     */
    public MatchDatabaseInterface() {
        matches = new HashMap<>();
        dRef = FirebaseDatabase.getInstance().getReference("matches");
        matchListener = new MatchEventListener();
        dRef.addChildEventListener(matchListener);
        completionListener = new MatchCompletionListener();
    }

    /**
     * Closes the listenner between the app and the Firebase server. Thus the updates in the database won't be seen in
     * the app
     */
    public void close() {
        dRef.removeEventListener(matchListener);
    }

    /**
     * Provides the set of match stored in the database
     *
     * @return returns the matches stored in the database as Map with the id of the match as key and the Match object as
     * value
     */
    public Map<String, Match> provide() {
        return matches;
    }

    /**
     * This method let us write a new match to the Firebase database
     *
     * @param matchToWrite the new match to write
     * @return returns the id of the match that was just written in the database
     */
    public String writeNewMatch(Match matchToWrite) {
        String key = dRef.push().getKey();
        dRef.child(key).setValue(matchToWrite, completionListener);
        return key;
    }

    /**
     * This method let us delete a match from the Firebase database
     *
     * @param token the id of the match that should be deleted
     */
    public void deleteMatch(String token) {
        dRef.child(token).removeValue(completionListener);
    }

    /**
     * This method let us update (write) a match to the database
     *
     * @param id    the id of the match that should be overwrite
     * @param match the data of the match that should be overwrite
     */
    public void updateMatch(String id, Match match) {
        dRef.child(id).setValue(match, completionListener);
    }

    /**
     * MatchCompletionListener is a class that let us know if an operation was not able to get to the server
     * It implements the CompletionListener
     */
    public class MatchCompletionListener implements CompletionListener {


        @Override
        public void onComplete(DatabaseError error, DatabaseReference ref) {
            if (error != null) {
                throw new ProviderException("Firebase operation did not complete");

            }
        }

    }

    /**
     * MatchEventListener is class that is needed to update our internal representation of the database. It listens to
     * any changes in the Firebase database and updates our data.
     * It implements the ChildEventListener
     */
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
        public void onCancelled(DatabaseError databaseError) {
        }
    }
}
