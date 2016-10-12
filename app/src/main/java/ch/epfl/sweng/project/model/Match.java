package ch.epfl.sweng.project.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Amaury Combes
 */

public class Match {
    private List<Player> players;
    private GPSPoint location;
    private String description;
    private MatchRank rank;
    private boolean privateMatch;
    private GameVariant gameVariant;
    private long expirationTime;

    Match() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Match(List<Player> players,
                 GPSPoint location,
                 String description,
                 MatchRank rank,
                 boolean privateMatch,
                 GameVariant gameVariant,
                 long expirationTime) {
        this.players = new ArrayList<Player>(players);
        this.location = location;
        this.description = description;
        this.rank = rank;
        this.privateMatch = privateMatch;
        this.gameVariant = gameVariant;
        this.expirationTime = expirationTime;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public GPSPoint getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public MatchRank getRank() {
        return rank;
    }

    public boolean isPrivateMatch() {
        return privateMatch;
    }

    public GameVariant getGameVariant() {
        return gameVariant;
    }

    public long getExpirationTime() {
        return expirationTime;
    }


    public static class MatchRank extends Rank {
        public MatchRank(int rank) {
            super(rank);
        }

        public MatchRank() {
        }
    }

    public enum GameVariant {CLASSIC}
}

