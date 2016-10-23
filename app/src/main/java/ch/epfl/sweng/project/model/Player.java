package ch.epfl.sweng.project.model;


import java.util.Objects;

/**
 * Player is class that represents a player
 */
public class Player {

    private PlayerID id;
    private String lastName;
    private String firstName;
    private Rank rank;

    /**
     * Default constructor required for calls to DataSnapshot.getValue when using Firebase
     */
    public Player() {
    }

    /**
     * Constructor of the Player class with no default assignment
     *
     * @param id        the id of the player (sciper)
     * @param lastName  the last name of the player
     * @param firstName the first name of the player
     * @param rank      the rank of the player
     */
    public Player(PlayerID id, String lastName, String firstName, Rank rank) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.rank = rank;
    }

    public Player(PlayerID id, String lastName, String firstName) {
        this(id, lastName, firstName, new PlayerRank(0));
    }

    /**
     * Getter for the id of the player
     *
     * @return returns the id of the player
     */
    public PlayerID getID() {
        return id;
    }

    /**
     * Getter for the last name of the player
     *
     * @return returns the last name of the player
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for the first name of the player
     *
     * @return returns the first name of the player
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for the rank of the player
     *
     * @return returns the rank of the player
     */
    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null || other.getClass() != this.getClass()) {
            return false;
        }

        Player otherPlayer = (Player) other;
        return this.id.getID() == otherPlayer.id.getID()
                && this.lastName.equals(otherPlayer.lastName)
                && this.firstName.equals(otherPlayer.firstName);
        // compare rank?
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.getID(), lastName, firstName);
    }

    /**
     * Sets the rank of the player
     *
     * @param newRank the new value of the rank we have to set
     */
    public void setRank(Rank newRank) {
        rank = newRank;
    }

    public static class PlayerRank extends Rank {

        public PlayerRank(int rank) {
            super(rank);
        }

        public PlayerRank() {
            // TODO get rank of last player
        }

    }

    public static class PlayerID extends ID {

        public PlayerID(long id) {
            super(id);
        }

        public PlayerID() {
        }

    }

}
