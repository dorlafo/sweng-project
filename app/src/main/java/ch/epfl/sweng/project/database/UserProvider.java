package ch.epfl.sweng.project.database;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.model.Player;

public class UserProvider {

    //UserProvider constructor
    //Make the connection to the user table
    public UserProvider() {

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
}
