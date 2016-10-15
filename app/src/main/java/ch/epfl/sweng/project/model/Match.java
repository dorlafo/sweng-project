package ch.epfl.sweng.project.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Match is a class that represents
 */
public class Match {
    private List<Player> players;
    private GPSPoint location;
    private String description;
    private Rank rank;
    private boolean privateMatch;
    private GameVariant gameVariant;
    private long expirationTime;

    /**
     * Default constructor required for calls to DataSnapshot.getValue when using Firebase
     */
    public Match() {

    }

    /**
     * A constructor of the Match class
     * @param players the list of players that are subscribed to the match
     * @param location the location of the match
     * @param description a brief description of the location of the match
     * @param privateMatch the visibility of the match (public or private)
     * @param gameVariant the variant of the match
     * @param expirationTime the time at which the match expires
     */
    public Match(List<Player> players,
                 GPSPoint location,
                 String description,
                 boolean privateMatch,
                 GameVariant gameVariant,
                 long expirationTime) {
        this.players = new ArrayList<>(players);
        this.location = location;
        this.description = description;
        this.privateMatch = privateMatch;
        this.gameVariant = gameVariant;
        this.expirationTime = expirationTime;
        rank = players.remove(0).average(players);
    }

    /**
     * Getter for the players' list of the match
     * @return returns the players' list of the match
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Getter for the location of the match
     * @return returns the location of the match in GPS format
     */
    public GPSPoint getLocation() {
        return location;
    }

    /**
     * Getter for the description of the match
     * @return returns the description of the match
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the rank of the match
     * @return returns the rank of the match
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Getter for the accessibility of the match
     * @return returns the accessibility of the match
     */
    public boolean isPrivateMatch() {
        return privateMatch;
    }

    /**
     * Getter for the variant of the match
     * @return returns the variant of the match
     */
    public GameVariant getGameVariant() {
        return gameVariant;
    }

    /**
     * Getter for the expiration date of the match
     * @return returns the expiration fate of the match
     */
    public long getExpirationTime() {
        return expirationTime;
    }

    /**
     * GameVariant is an enumaration that represents the various game variant of a match
     */
    public enum GameVariant {CLASSIC}
}

