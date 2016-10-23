package ch.epfl.sweng.project.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.project.model.Player;

public class UserProvider {
    private final DatabaseReference dRef;
    private final Map<String, Player> players;
    private final List<ChildEventListener> playerListeners;

    /**
     * UserProvider constructor
     * Make the link to the "players" table and add an internal listener
     */
    public UserProvider() {
        players = new HashMap<>();
        dRef = FirebaseDatabase.getInstance().getInstance().getReference().child("players");
        ChildEventListener playerListenerInternal = new PlayerEventListener();
        dRef.addChildEventListener(playerListenerInternal);
        playerListeners = new ArrayList<>();
        playerListeners.add(playerListenerInternal);
    }

    /**
     * Close the connection to the user table and remove all added listener
     */
    public void close() {
        for(ChildEventListener cel : playerListeners) {
            dRef.removeEventListener(cel);
        }
        playerListeners.clear();
    }

    /**
     * Getter for the players list
     * @return A list containing the players
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(new ArrayList<>(players.values()));
    }


    /**
     * Getter for a player with a specific ID
     * @param playerID the ID for the player we want to retrieve
     * @return A player or NULL if the player is not in the list
     */
    public Player getPlayerWithID(Player.PlayerID playerID) {
        return players.get(playerID.toString());
    }

    /**
     * Getter for a player with a specific ID
     * @param playerID the ID for the player we want to retrieve
     * @return A player or NULL if the player is not in the list
     */
    public Player getPlayerWithID(String playerID) {
        return players.get(playerID);
    }

    /**
     * Add a player to the database
     * @param p The player to add
     * @return The key that references this player on the database
     */
    public String addPlayer(Player p) {
        dRef.child(p.getID().toString()).setValue(p);
        return dRef.getKey();
    }

    /**
     * Modify a player in the database with the data in p
     * @param playerID the id of the player we want to modify
     * @param p The new players' data
     */
    public void modifyPlayer(Player.PlayerID playerID, Player p) {
        dRef.child(playerID.toString()).setValue(p);
    }

    /**
     * Delete a user from the database
     * @param playerID the player's ID to delete
     */
    public void deletePlayer(Player.PlayerID playerID) {
        dRef.child(playerID.toString()).removeValue();
    }

    /**
     * Add a ChildEventListener to the database reference
     * @param cel the ChildEventListener to add
     */
    public void addEventListener(ChildEventListener cel) {
        playerListeners.add(cel);
        dRef.addChildEventListener(cel);
    }

    /**
     * EventListener that modify the players hash map when new data come from database
     */
    private class PlayerEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Player p = dataSnapshot.getValue(Player.class);
            players.put(dataSnapshot.getKey(), p);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Player p = dataSnapshot.getValue(Player.class);
            players.put(dataSnapshot.getKey(), p);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            players.remove(dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //Nothing specific to do
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //Nothing specific to do
        }
    }
}
