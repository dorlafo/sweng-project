package ch.epfl.sweng.project.model;

import java.util.Date;
import java.util.List;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author Amaury Combes
 */

public class Match {
    private List<Player> players;
    private LatLng location;
    private String description;
    private MatchRank rank;
    private boolean privateMatch;
    private GameVariant gameVariant;
    private Date expirationTime;

    public List<Player> getPlayers() {
        return players;
    }

    public LatLng getLocation() {
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

    public Date getExpirationTime() {
        return expirationTime;
    }

    public Match(List<Player> players,
                 LatLng location,
                 String description,
                 MatchRank rank,
                 boolean privateMatch,
                 GameVariant gameVariant,
                 Date expirationTime) {
        this.players = players;
        this.location = location;
        this.description = description;
        this.rank = rank;
        this.privateMatch = privateMatch;
        this.gameVariant = gameVariant;
        this.expirationTime = expirationTime;
    }

    Match() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public static class MatchRank extends Rank {
        public MatchRank(int rank) {
            super(rank);
        }
        public MatchRank() { };
    }

    public enum GameVariant {CLASSIC}
}

