package ch.epfl.sweng.project.model;

public class Player {
    private PlayerID id;
    private String lastName;
    private String firstName;
    private PlayerRank rank;

    public Player() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public Player(PlayerID id, String lastName, String firstName, PlayerRank rank) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.rank = rank;
    }

    public PlayerID getID() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public PlayerRank getRank() {
        return rank;
    }

    public void setRank(PlayerRank newRank) {
        this.rank = newRank;
    }

    public static class PlayerID extends ID {
        public PlayerID(long sciper) {
            super(sciper);
        }
        public PlayerID() {}
    }

    public static class PlayerRank extends Rank {
        public PlayerRank(int rank) {
            super(rank);
        }

        public PlayerRank() { }
    }
}
