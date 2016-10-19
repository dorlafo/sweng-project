package ch.epfl.sweng.project.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.model.Player;

public class UserProvider {
    private final DatabaseReference dRef;

    //UserProvider constructor
    //Make the connection to the user table
    public UserProvider() {
        dRef = FirebaseDatabase.getInstance().getInstance().getReference("players");
    }

    //Close the connection to the user table
    public void close() {

    }

    //Get the user list
    public List<Player> providePlayers() {
        List<Player> players = new ArrayList<>();

        return players;
    }

    //Get one user
    public Player providePlayerWithID(Player.PlayerID playerID) {
        Player p = new Player();

        return p;
    }

    //Add the user p to the database
    public void addUser(Player p) {

    }

    //Modify the user playedID with the infos contained in p in the database
    public void modifyUser(Player.PlayerID playerID, Player p) {

    }

    //Delete the user playerID in the database
    public void deleteUser(Player.PlayerID playerID) {

    }
}
