package ch.epfl.sweng.project.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ch.epfl.sweng.project.model.Match.GameVariant.CLASSIC;
import static ch.epfl.sweng.project.tools.RankOperationsHelper.averageRank;

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
    private int maxPlayerNumber;
    private long expirationTime;

    /**
     * Default constructor required for calls to DataSnapshot.getValue when using Firebase
     */
    public Match() {
    }

    /**
     * A constructor of the Match class
     *
     * @param players        the list of players that are subscribed to the match
     * @param location       the location of the match
     * @param description    a brief description of the location of the match
     * @param privateMatch   the visibility of the match (public or private)
     * @param gameVariant    the variant of the match
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
        rank = averageRank(players);
        this.privateMatch = privateMatch;
        this.gameVariant = gameVariant;
        this.maxPlayerNumber = gameVariant.getMaxPlayerNumber();
        this.expirationTime = expirationTime;
    }

    public Match(List<Player> players,
                 GPSPoint location,
                 String description,
                 boolean privateMatch,
                 long expirationTime) {
        this(players, location, description, privateMatch, CLASSIC, expirationTime);
    }

    /**
     * Getter for the players' list of the match
     *
     * @return returns the players' list of the match
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Getter for the location of the match
     *
     * @return returns the location of the match in GPS format
     */
    public GPSPoint getLocation() {
        return location;
    }

    /**
     * Getter for the description of the match
     *
     * @return returns the description of the match
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the rank of the match
     *
     * @return returns the rank of the match
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Getter for the accessibility of the match
     *
     * @return returns the accessibility of the match
     */
    public boolean isPrivateMatch() {
        return privateMatch;
    }

    /**
     * Getter for the variant of the match
     *
     * @return returns the variant of the match
     */
    public GameVariant getGameVariant() {
        return gameVariant;
    }

    public int getMaxPlayerNumber() {
        return maxPlayerNumber;
    }

    /**
     * Getter for the expiration date of the match
     *
     * @return returns the expiration fate of the match
     */
    public long getExpirationTime() {
        return expirationTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null || other.getClass() != this.getClass()) {
            return false;
        }

        Match otherMatch = (Match) other;

        return this.players.equals(otherMatch.players) // does order matter?
                && this.location.equals(otherMatch.location)
                && this.description.equals(otherMatch.description)
                // compare rank?
                && this.privateMatch == otherMatch.privateMatch
                && this.gameVariant == otherMatch.gameVariant
                && this.maxPlayerNumber == otherMatch.maxPlayerNumber
                && this.expirationTime == otherMatch.expirationTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(players, location, description, privateMatch,
                gameVariant, maxPlayerNumber, expirationTime);
    }

    public static class MatchRank extends Rank {
        public MatchRank(int rank) {
            super(rank);
        }
    }

    /**
     * Getter for the creator of the game.
     *
     * @return the creator of the game
     */
    public Player createdBy() {
        return players.get(0);
    }

    /**
     * GameVariant is an enumeration that represents the various game variants of a match
     */
    public enum GameVariant {
        CLASSIC("Classic");

        private final String variantName;

        GameVariant(String variantName) {
            this.variantName = variantName;
        }

        @Override
        public String toString() {
            return variantName;
        }

        public int getMaxPlayerNumber() {
            switch (this) {
                case CLASSIC:
                default:
                    return 4;
            }
        }

    }

    public static final class Builder {

        public static final String DEFAULT_DESCRIPTION = "New Match";

        private List<Player> players;
        private GPSPoint location;
        private String description;
        private boolean privateMatch;
        private GameVariant gameVariant;
        private int maxPlayerNumber;
        private long expirationTime;

        public Builder() {
            players = new ArrayList<>();
            location = new GPSPoint(46.520407, 6.565802); // Esplanade
            description = DEFAULT_DESCRIPTION;
            privateMatch = false;
            gameVariant = CLASSIC;
            maxPlayerNumber = CLASSIC.getMaxPlayerNumber();
            expirationTime = Calendar.getInstance().getTimeInMillis() + 2 * 3600 * 1000; // 2 hours after current time
        }

        public Builder addPlayer(Player player) {
            if (players.size() >= maxPlayerNumber) {
                throw new IllegalStateException("Match is full.");
            }
            if (!players.contains(player)) {
                players.add(player);
            }
            return this;
        }

        // TODO: add removePlayer method

        public Builder setLocation(GPSPoint location) {
            this.location = location;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrivacy(boolean privateMatch) {
            this.privateMatch = privateMatch;
            return this;
        }

        public Builder setVariant(GameVariant gameVariant) {
            this.gameVariant = gameVariant;
            maxPlayerNumber = gameVariant.getMaxPlayerNumber();
            return this;
        }

        public Builder setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        // TODO: check validity of arguments
        public Match build() throws IllegalStateException {
            if (players.isEmpty()) {
                throw new IllegalStateException("Cannot create match without any player.");
            } else if (players.size() > maxPlayerNumber) {
                throw new IllegalStateException("Too many players.");
            } else {
                return new Match(players, location, description, privateMatch,
                        gameVariant, expirationTime);
            }
        }

    }
}
