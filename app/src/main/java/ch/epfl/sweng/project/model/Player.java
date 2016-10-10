package ch.epfl.sweng.project.model;

public class Player {
    private final PlayerID id;
    private final String lastName;
    private final String firstName;
    private PlayerRank rank;

    public Player(PlayerID id, String lastName, String firstName, PlayerRank rank) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.rank = rank;
    }

    public Player(PlayerID id, String lastName, String firstName) {
        this(id, lastName, firstName, new PlayerRank());
    }

    public PlayerID getId() {
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

    @Override
    public String toString() {
        return firstName + ' ' + lastName;
    }

    public static class PlayerID extends ID {
        public PlayerID(long id) {
            super(id);
        }
    }

    public static class PlayerRank extends Rank {
        public PlayerRank(int rank) {
            super(rank);
        }

        public PlayerRank() {
            // TODO get rank of last player
            super(0);
        }
    }
}
