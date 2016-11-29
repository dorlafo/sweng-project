package ch.epfl.sweng.jassatepfl.model;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static ch.epfl.sweng.jassatepfl.model.Match.GameVariant.CHIBRE;
import static ch.epfl.sweng.jassatepfl.tools.RankOperationsHelper.averageRank;

/**
 * Class representing a match between {@link ch.epfl.sweng.jassatepfl.model.Player players}.
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
    private String matchID;
    private MatchStatus matchStatus;
    private Map<String, List<String>> teams;

    /**
     * Default constructor required for calls to DataSnapshot.getValue when using Firebase.
     */
    public Match() {
    }

    /**
     * Constructs a Match with a given variant and a given status.
     *
     * @param players        The list of players in the match
     * @param location       The location of the match
     * @param description    A brief description of the match (detailed location...)
     * @param privateMatch   The visibility of the match (public or private)
     * @param gameVariant    The variant of the match
     * @param expirationTime The time at which the match expires (in milliseconds after epoch)
     * @param matchID        The unique firebase ID of the match
     * @param status         The status of the match
     */
    public Match(List<Player> players,
                 GPSPoint location,
                 String description,
                 boolean privateMatch,
                 GameVariant gameVariant,
                 long expirationTime,
                 String matchID,
                 MatchStatus status) {
        this.players = new ArrayList<>(players);
        this.location = location;
        this.description = description;
        rank = averageRank(players);
        this.privateMatch = privateMatch;
        this.gameVariant = gameVariant;
        this.maxPlayerNumber = gameVariant.getMaxPlayerNumber();
        this.expirationTime = expirationTime;
        this.matchID = matchID;
        this.matchStatus = status;
        this.teams = new HashMap<>();
        for(int teamNb = 0; teamNb < gameVariant.getNumberOfTeam(); ++teamNb) {
            //If we don't do this, Firebase will not add the team map since the value are 0.....
            //We need the key to be a string without beginning by a number of Firebase with think it's an array...
            this.teams.put("Team" + Integer.toString(teamNb), new ArrayList<String>());

            this.teams.get("Team" + Integer.toString(teamNb)).add("42");
        }
    }

    /**
     * Constructs a Match with a given variant and the default status (pending).
     *
     * @param players        The list of players in the match
     * @param location       The location of the match
     * @param description    A brief description of the match (detailed location...)
     * @param privateMatch   The visibility of the match (public or private)
     * @param gameVariant    The variant of the match
     * @param expirationTime The time at which the match expires (in milliseconds after epoch)
     * @param matchID        The unique firebase ID of the match
     */
    public Match(List<Player> players,
                 GPSPoint location,
                 String description,
                 boolean privateMatch,
                 GameVariant gameVariant,
                 long expirationTime,
                 String matchID) {
        this(players, location, description, privateMatch, gameVariant,expirationTime, matchID, MatchStatus.PENDING);
    }

    /**
     * Constructs a Match with default variant (Chibre) and the default status (pending).
     *
     * @param players        The list of players in the match
     * @param location       The location of the match
     * @param description    A brief description of the match (detailed location...)
     * @param privateMatch   The visibility of the match (public or private)
     * @param expirationTime The time at which the match expires (in milliseconds after epoch)
     * @param matchID        The unique firebase ID of the match
     */
    public Match(List<Player> players,
                 GPSPoint location,
                 String description,
                 boolean privateMatch,
                 long expirationTime,
                 String matchID) {
        this(players, location, description, privateMatch, CHIBRE, expirationTime, matchID);
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
     * Getter for the player's index in the players list
     * @param id The player' id
     * @return The index of the player if he is in match, -1 otherwise
     */
    public int getPlayerIndex(Player.PlayerID id) {
        for(int i = 0; i < players.size(); ++i) {
            if(players.get(i).getID().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public static Match sentinelMatch() {
        Player bricoloBob = new Player(new Player.PlayerID(696969), "LeBricoleur", "Bob", new Rank(1000));
        List<Player> players = new ArrayList<>();
        players.add(bricoloBob);
        GPSPoint BCCoord = new GPSPoint(46.518470, 6.561907);
        return new Match(players, BCCoord, "Sentinel match", true,
                GameVariant.CHIBRE, Calendar.getInstance().getTimeInMillis() + 2 * 3600 * 1000,
                "sentiMatch", MatchStatus.PENDING);
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

    /**
     * Getter for the firebase ID of the match.
     *
     * @return The firebase ID of the match
     */
    public String getMatchID() {
        return matchID;
    }

    public boolean matchFull() {
        return players.size() == getMaxPlayerNumber();
    }

    /**
     * Set the player with the specified id in the specified team
     * @param teamNb The team we want to add a player
     * @param id The id of the player
     * @throws IllegalArgumentException If the team number is not valid or if the id is not one of a match player
     */
    public void setTeam(int teamNb, Player.PlayerID id) throws IllegalArgumentException {
        List<String> team = teams.get("Team" + Integer.toString(teamNb));
        if(team == null) {
            throw new IllegalArgumentException("Invalid team number specified.");
        }
        if(hasParticipantWithID(id))
        if(team.contains("42")) {
            team.set(team.indexOf("42"), id.toString());
        }
        else if(!team.contains(id.toString())) {
            team.add(id.toString());
        }
        //remove it from other team if he was in one
        for(List<String> t : teams.values()) {
            if(t != team) {
                t.remove(id.toString());
                if(t.isEmpty()) {
                    t.add("42");
                }
            }
        }
    }

    public Map<String, List<String>> getTeams() {
        return teams;
    }

    /**
     * Getter for the match' status
     *
     * @return True if the match is active, false if it is waiting for players
     */
    public MatchStatus getMatchStatus() {
        return matchStatus;
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null || other.getClass() != this.getClass()) {
            return false;
        }

        Match otherMatch = (Match) other;

        return this.matchID.equals(otherMatch.matchID);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(matchID);
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
     * Adds the given player to the match.
     * <br>
     * Adding a player that is already present in the match does nothing,
     * and adding a player if the match if full throws an exception.
     *
     * @param player The player to add to the match
     * @throws IllegalStateException  When the match is full
     * @throws IllegalAccessException When the player is already in the match
     */
    public void addPlayer(Player player) throws IllegalStateException, IllegalAccessException {
        if (players.size() >= maxPlayerNumber) {
            throw new IllegalStateException("Match is full.");
        }
        if (!players.contains(player)) {
            players.add(player);
        } else {
            throw new IllegalAccessException("Player already in that Match.");
        }
    }

    /**
     * Removes the given player to the player list and change the match status to pending
     *
     * @param toRemove The id of the player to remove from the match
     * @return True if the player was removed, false otherwise
     */
    public boolean removePlayerById(Player.PlayerID toRemove) {
        if (!players.isEmpty()) {
            int index = -1;

            for (Player p : players) {
                if (p.getID().equals(toRemove)) {
                    index = players.indexOf(p);
                }
            }
            if (index != -1) {
                players.remove(index);
                matchStatus = MatchStatus.PENDING;
            }
            //Remove player from team if he was in one
            for(List<String> t : teams.values()) {
                t.remove(toRemove.toString());
                if(t.isEmpty()) {
                    t.add("42");
                }
            }
            return true;
        }
        return false;
    }

    public boolean teamAssignmentIsCorrect() {
        //Test if all player of the match are in the team list and no more
        Set<String> assignedPlayer = new HashSet<>();
        //Correct number of team
        if(teams.size() != gameVariant.getNumberOfTeam()) {
            return false;
        }

        //For all team
        for(List<String> team : teams.values()) {
            //Correct number of player by team
            if(team.size() != gameVariant.getNumberOfPlayerByTeam()) {
                return false;
            }
            //The team does not contain the sentinel
            else if(team.contains("42")) {
                return false;
            }
            assignedPlayer.addAll(team);
        }

        //The teams does not contain duplicates
        if(assignedPlayer.size() != players.size()) {
            return false;
        }
        else {
            //Every player from the team is in the match
            for(String s : assignedPlayer) {
                if(!hasParticipantWithID(new Player.PlayerID(s))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setStatus(MatchStatus status) {
        this.matchStatus = status;
    }

    /**
     * Checks whether the given player is taking part in the match.
     *
     * @param player The player
     * @return true if the player is in the match, false otherwise
     */
    public boolean hasParticipant(Player player) {
        return players.contains(player);
    }

    /**
     * Checks if the match has a participant with the given ID
     *
     * @param userID The ID to check
     * @return true if a player with the id is in the match, false otherwise
     */
    public boolean hasParticipantWithID(Player.PlayerID userID) {
        for(Player p : players) {
            if(p.getID().equals(userID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the team number the player is in
     * @param p The player
     * @return the team number if the player is in the match, -1 otherwise
     */
    public int teamNbForPlayer(Player p) {
        if(players.contains(p)) {
            for(int teamNb = 0; teamNb < teams.size(); teamNb++) {
                if(teams.get("Team" + teamNb).contains(p.getID().toString())) {
                    return teamNb+1;
                }
            }
        }
        return -1;
    }

    public static class MatchRank extends Rank {

        public MatchRank(int rank) {
            super(rank);
        }

    }

    /**
     * The different status a match can have
     */
    public enum MatchStatus {
        PENDING("pending"),
        ACTIVE("active");

        private final String statusName;

        MatchStatus(String statusName) {
            this.statusName = statusName;
        }

        @Override
        public String toString() {
            return statusName;
        }
    }

    /**
     * The different meld available
     */
    public enum Meld {
        SENTINEL("Sentinel"),
        LAST_TRICK("Cinq de der"),
        MARRIAGE("Stöck"),
        THREE_CARDS("Trois cartes"),
        FIFTY("Cinquante"),
        HUNDRED("Cent"),
        FOUR_NINE("Cent cinquante"),
        FOUR_JACKS("Deux cent");

        private final String meldName;

        Meld(String meldName) {
            this.meldName = meldName;
        }

        @Override
        public String toString() {
            return meldName;
        }

        /**
         * Returns the value of the current meld
         *
         * @return The value of the meld
         */
        public int value() {
            switch (this) {
                case LAST_TRICK:
                    return 5;
                case MARRIAGE:
                case THREE_CARDS:
                    return 20;
                case FIFTY:
                    return 50;
                case HUNDRED:
                    return 100;
                case FOUR_NINE:
                    return 150;
                case FOUR_JACKS:
                    return 200;
                case SENTINEL:
                default:
                    return 0;
            }
        }

    }

    /**
     * The different variants of a Jass game.
     */
    public enum GameVariant {
        CHIBRE("Chibre"),
        PIQUE_DOUBLE("Pique Double"),
        OBEN_ABE("Oben Abe"),
        UNDEN_UFE("Unden Ufe"),
        SLALOM("Slalom"),
        CHICANE("Chicane"),
        JASS_MARANT("Jass Marant"),
        ROI("Roi"),
        POMME("Pomme");

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
                case POMME:
                    return 2;
                case ROI:
                    return 3;
                case CHIBRE:
                case PIQUE_DOUBLE:
                case OBEN_ABE:
                case UNDEN_UFE:
                case SLALOM:
                case CHICANE:
                case JASS_MARANT:
                default:
                    return 4;
            }
        }

        /**
         * Returns the number of team for the current game variant
         *
         * @return The number of team
         */
        public int getNumberOfTeam() {
            switch (this) {
                case ROI:
                    return 3;
                case CHIBRE:
                case PIQUE_DOUBLE:
                case OBEN_ABE:
                case UNDEN_UFE:
                case SLALOM:
                case CHICANE:
                case JASS_MARANT:
                case POMME:
                default:
                    return 2;
            }
        }

        /**
         * Returns the number of player by team for the current game variant
         *
         * @return The number of player by team
         */
        public int getNumberOfPlayerByTeam() {
            switch (this) {
                case ROI:
                case POMME:
                    return 1;
                case CHIBRE:
                case PIQUE_DOUBLE:
                case OBEN_ABE:
                case UNDEN_UFE:
                case SLALOM:
                case CHICANE:
                case JASS_MARANT:
                default:
                    return 2;
            }
        }

        public int getPointGoal() {
            switch (this) {
                case ROI:
                case POMME:
                    return 20;
                case CHIBRE:
                    return 1000;
                case PIQUE_DOUBLE:
                    return 1500;
                case OBEN_ABE:
                case UNDEN_UFE:
                case SLALOM:
                case CHICANE:
                case JASS_MARANT:
                default:
                    return 2500;
            }
        }
    }

    /**
     * Builder for the Match class.
     */
    public static final class Builder {

        public static final String DEFAULT_DESCRIPTION = "New Match";
        public static final String DEFAULT_ID = "Default Match ID";

        private final List<Player> players;
        private GPSPoint location;
        private String description;
        private boolean privateMatch;
        private GameVariant gameVariant;
        private int maxPlayerNumber;
        private long expirationTime;
        private String matchID;
        private MatchStatus matchStatus;

        /**
         * Constructs a new match builder with default values, but with an empty player list.
         */
        public Builder() {
            players = new ArrayList<>();
            location = new GPSPoint(46.520450, 6.567737); // Satellite
            description = DEFAULT_DESCRIPTION;
            privateMatch = false;
            gameVariant = CHIBRE;
            maxPlayerNumber = CHIBRE.getMaxPlayerNumber();
            expirationTime = Calendar.getInstance().getTimeInMillis() + 1 * 3600 * 1000; // 1 hour after current time
            matchID = DEFAULT_ID;
            matchStatus = MatchStatus.PENDING;
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
        public Builder addPlayer(Player player) throws IllegalStateException, IllegalAccessException {
            if (players.size() >= maxPlayerNumber) {
                throw new IllegalStateException("Match is full.");
            }
            if (!players.contains(player)) {
                players.add(player);
            } else {
                throw new IllegalAccessException("Player already in that Match.");
            }
            return this;
        }

        public void removePlayer(Player player) throws IllegalStateException, IllegalArgumentException {
            if (players.isEmpty()) {
                throw new IllegalStateException("No players in the match.");
            }
            if (!players.contains(player)) {
                throw new IllegalArgumentException("Player not in the Match.");
            }
            players.remove(player);
            matchStatus = MatchStatus.PENDING;
        }

        public List<Player> getPlayerList() {
            return new ArrayList<>(players);
        }

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

        public Builder setMatchID(String matchID) {
            this.matchID = matchID;
            return this;
        }

        public Builder setStatus(MatchStatus status) {
            this.matchStatus = status;
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
                        gameVariant, expirationTime, matchID, matchStatus);
            }
        }

    }

}
