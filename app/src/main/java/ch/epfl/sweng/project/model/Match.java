package ch.epfl.sweng.project.model;

import java.util.Date;
import java.util.List;

/**
 * @author Amaury Combes
 */

public abstract class Match {
    private final MatchID id;
    private List<Player> players;
    private gpsCoordinates location;
    private String description;
    private MatchRank rank;
    private boolean privateMatch;
    private GameVariant gameVariant;
    private Date expirationTime;

    public Match(MatchID id,
                 List<Player> players,
                 gpsCoordinates location,
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
