package ch.epfl.sweng.project.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

/**
 * @author Amaury Combes
 */

public abstract class Match {
    private List<Player> players;
    private GPSPoint location;
    private String description;
    private MatchRank rank;
    private boolean privateMatch;
    private GameVariant gameVariant;
    private Date expirationTime;

    public List<Player> getPlayers() {
        return players;
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

    public Date getExpirationTime() {
        return expirationTime;
    }

    Match(List<Player> players,
          GPSPoint location,
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

    private static class MatchRank extends Rank {
        MatchRank(int rank) {
            super(rank);
        }
        MatchRank() { };
    }

    enum GameVariant {CLASSIC}
}

