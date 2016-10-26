package ch.epfl.sweng.project.model;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ch.epfl.sweng.project.model.Match.GameVariant.CLASSIC;
import static ch.epfl.sweng.project.tools.RankOperationsHelper.averageRank;

/**
 * Class representing a match between {@link ch.epfl.sweng.project.model.Player players}.
 * Its rank is the mean of the ranks of the players in the match.
 * A match has an expiration date (the time before it is no longer available), and can be
 * private.
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
     * Default constructor required for calls to DataSnapshot.getValue when using Firebase.
     */
    public Match() {
    }

    /**
     * Constructs a Match with a given variant.
     *
     * @param players        The list of players in the match
     * @param location       The location of the match
     * @param description    A brief description of the match (detailed location...)
     * @param privateMatch   The visibility of the match (public or private)
     * @param gameVariant    The variant of the match
     * @param expirationTime The time at which the match expires (in milliseconds after epoch)
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

    /**
     * Constructs a Match with default variant (Classic).
     *
     * @param players        The list of players in the match
     * @param location       The location of the match
     * @param description    A brief description of the match (detailed location...)
     * @param privateMatch   The visibility of the match (public or private)
     * @param expirationTime The time at which the match expires (in milliseconds after epoch)
     */
    public Match(List<Player> players,
                 GPSPoint location,
                 String description,
                 boolean privateMatch,
                 long expirationTime) {
        this(players, location, description, privateMatch, CLASSIC, expirationTime);
    }

    /**
     * Getter for the players' list of the match.
     *
     * @return The players' list of the match
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Getter for the location of the match.
     *
     * @return The location of the match in GPS format
     */
    public GPSPoint getLocation() {
        return location;
    }

    /**
     * Getter for the description of the match.
     *
     * @return The description of the match
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the rank of the match.
     *
     * @return The rank of the match
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Getter for the visibility of the match.
     *
     * @return The visibility of the match
     */
    public boolean isPrivateMatch() {
        return privateMatch;
    }

    /**
     * Getter for the variant of the match.
     *
     * @return The variant of the match
     */
    public GameVariant getGameVariant() {
        return gameVariant;
    }

    /**
     * Getter for the maximum number of players allowed in
     * the match, determined by the variant.
     *
     * @return The maximum number of players
     */
    public int getMaxPlayerNumber() {
        return maxPlayerNumber;
    }

    /**
     * Getter for the expiration date of the match.
     *
     * @return The expiration date of the match, in milliseconds from epoch
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
                && Math.abs(this.expirationTime - otherMatch.expirationTime) < 60 * 1000;
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
     * Getter for the creator of the match, its first player.
     *
     * @return The creator of the match
     */
    public Player createdBy() {
        return players.get(0);
    }

    /**
     * The different variants of a Jass game.
     * <li>{@link #CLASSIC}</li>
     */
    public enum GameVariant {
        /**
         * Classic Jass game, with 4 players.
         */
        CLASSIC("Classic");

        private final String variantName;

        GameVariant(String variantName) {
            this.variantName = variantName;
        }

        @Override
        public String toString() {
            return variantName;
        }

        /**
         * Returns the max number of players allowed in the current variant.
         *
         * @return The max number of players
         */
        public int getMaxPlayerNumber() {
            switch (this) {
                case CLASSIC:
                default:
                    return 4;
            }
        }

    }

    /**
     * Builder for the Match class.
     */
    public static final class Builder {

        public static final String DEFAULT_DESCRIPTION = "New Match";

        private List<Player> players;
        private GPSPoint location;
        private String description;
        private boolean privateMatch;
        private GameVariant gameVariant;
        private int maxPlayerNumber;
        private long expirationTime;

        /**
         * Constructs a new match builder with default values, but with an empty player list.
         */
        public Builder() {
            players = new ArrayList<>();
            location = new GPSPoint(46.520407, 6.565802); // Esplanade
            description = DEFAULT_DESCRIPTION;
            privateMatch = false;
            gameVariant = CLASSIC;
            maxPlayerNumber = CLASSIC.getMaxPlayerNumber();
            expirationTime = Calendar.getInstance().getTimeInMillis() + 2 * 3600 * 1000; // 2 hours after current time
        }

        /**
         * Adds the given player to the player list.
         * <p>
         * Adding a player that is already present in the list does nothing,
         * and adding a player if the match if full throws an exception.
         *
         * @param player The player to add to the match
         * @return The updated builder
         */
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

        /**
         * Sets the game variant to the given value.
         * <p>
         * Changes the max player number according to the new variant.
         *
         * @param gameVariant The new game variant
         * @return The updated builder
         */
        public Builder setVariant(GameVariant gameVariant) {
            this.gameVariant = gameVariant;
            maxPlayerNumber = gameVariant.getMaxPlayerNumber();
            return this;
        }

        public Builder setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        /**
         * Builds and returns the match.
         * <p>
         * Building a match with an empty player list or with more players than allowed
         * throws an exception
         *
         * @return The new match
         * @throws IllegalStateException If building with no players or too many players
         */
        public Match build() throws IllegalStateException {
            // TODO: check validity of arguments
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
