package ch.epfl.sweng.project.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * @author Amaury Combes
 */

public abstract class Match {
    private final MatchID id;
    private List<Player> players;
    private LatLng location;
    private String description;
    private MatchRank rank;
    private boolean privateMatch;
    private GameVariant gameVariant;
    private Date expirationTime;

    protected Match(MatchID id,
                 List<Player> players,
                 LatLng location,
                 String description,
                 MatchRank rank,
                 boolean privateMatch,
                 GameVariant gameVariant,
                 Date expirationTime)
    {
        this.id = id;
        this.players = players;
        this.location = location;
        this.description = description;
        this.rank = rank;
        this.privateMatch = privateMatch;
        this.gameVariant = gameVariant;
        this.expirationTime = expirationTime;
    }

    public LatLng getLocation() {
        return location;
    }

    private static class MatchRank extends Rank {
        MatchRank(int rank) {
            super(rank);
        }
    }

    private static class MatchID extends ID {
        public MatchID(long id) {
            super(id);
        }
    }

    private enum GameVariant {

    }
}
