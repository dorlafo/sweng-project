package model;

public class Player {
    private final PlayerID id;
    private final String lastName;
    private final String firstName;
    private PlayerRank rank;

    Player(PlayerID id, String lastName, String firstName, PlayerRank rank) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.rank = rank;
    }

    Player(PlayerID id, String lastName, String firstName) {
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

    private static class PlayerID extends ID {
        PlayerID(long id) {
            super(id);
        }
    }

    private static class PlayerRank extends Rank {
        PlayerRank(int rank) {
            super(rank);
        }

        PlayerRank() {
            // TODO get rank of last player
            super(0);
        }
    }
}
