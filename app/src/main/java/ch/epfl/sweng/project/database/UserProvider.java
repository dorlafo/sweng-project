package ch.epfl.sweng.project.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.project.model.Player;

public class UserProvider {
    private final DatabaseReference dRef;
    //private final ChildEventListener playerListener;
    private final Map<String, Player> players;
    private final ChildEventListener[] playerListeners;

    //UserProvider constructor
    //Make the connection to the user table
    public UserProvider(ChildEventListener... playerListeners) {
        players = new HashMap<>();
        dRef = FirebaseDatabase.getInstance().getInstance().getReference().child("players");
        //playerListener = new PlayerEventListener();
        this.playerListeners = playerListeners;
        for(ChildEventListener cel : this.playerListeners) {
            dRef.addChildEventListener(cel);
        }

        /*
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    Player p = playerSnapshot.getValue(Player.class);
                    System.out.println("Players updated player: " + p);
                    players.put(p.getID().toString(), p);
                }
                // Get Post object and use the values to update the UI
                //Player p = dataSnapshot.getValue(Player.class);
                //players.put(dataSnapshot.getKey(), p);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("onCancelled from ValueEventListener");
            }
        };
        dRef.addListenerForSingleValueEvent(postListener);
        */
    }

    //Close the connection to the user table
    public void close() {
        for(ChildEventListener cel : playerListeners) {
            dRef.removeEventListener(cel);
        }
    }

    //Get the user list
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(new ArrayList<>(players.values()));
    }

    //Get one user
    public Player getPlayerWithID(Player.PlayerID playerID) {
        return players.get(playerID.toString());
    }

    //Add the user p to the database and return its key
    public String addUser(Player p) {
        dRef.child(p.getID().toString()).setValue(p);
        return dRef.getKey();
    }

    //Modify the user playedID with the infos contained in p in the database
    public void modifyUser(Player.PlayerID playerID, Player p) {
        dRef.child(playerID.toString()).setValue(p);
    }

    //Delete the user playerID in the database
    public void deleteUser(Player.PlayerID playerID) {
        dRef.getRoot().child(playerID.toString()).removeValue();
    }

    /*private class PlayerEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Player p = dataSnapshot.getValue(Player.class);
            players.put(dataSnapshot.getKey(), p);
            System.out.println("PLAYER-EVENT-LISTENER----: ON-CHILD-ADDED");
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Player p = dataSnapshot.getValue(Player.class);
            players.put(dataSnapshot.getKey(), p);
            System.out.println("PLAYER-EVENT-LISTENER----: ON-CHILD-cCHANGED");
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
    }*/
}
