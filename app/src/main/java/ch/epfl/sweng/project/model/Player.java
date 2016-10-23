package ch.epfl.sweng.project.model;

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

    /**
     * Sets the rank of the player
     *
     * @param newRank the new value of the rank we have to set
     */
    public void setRank(Rank newRank) {
        rank = newRank;
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName;
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
