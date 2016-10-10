package ch.epfl.sweng.project.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * @author Amaury Combes
 */

public class Match {
    // TODO: make this immutable
    private final MatchID id;
    private List<Player> players;
    private LatLng location;
    private String description;
    private MatchRank rank;
    private boolean privateMatch;
    private GameVariant gameVariant;
    private Date expirationTime;

    public Match(MatchID id,
                 List<Player> players,
                 LatLng location,
                 String description,
                 MatchRank rank,
                 boolean privateMatch,
                 GameVariant gameVariant,
                 Date expirationTime) {
        this.id = id;
        this.players = players;
        this.location = location;
        this.description = description;
        this.rank = rank;
        this.privateMatch = privateMatch;
        this.gameVariant = gameVariant;
        this.expirationTime = expirationTime;
    }

    public MatchID getID() {
        return id;
    }

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

    public static class MatchRank extends Rank {
        public MatchRank(int rank) {
            super(rank);
        }
    }

    public static class MatchID extends ID {
        public MatchID(long id) {
            super(id);
        }
    }

    private enum GameVariant {

    }
}
