package ch.epfl.sweng.project.model;

import java.util.ArrayList;
import java.util.List;


public class Player implements Averageable<Player, Rank> {
    private PlayerID id;
    private String lastName;
    private String firstName;
    private Rank rank;

    /**
     * Default constructor required for calls to DataSnapshot.getValue when using Firebase
     */
    public Player() {

    }

    public Player(PlayerID id, String lastName, String firstName, Rank rank) {
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

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank newRank) {
        this.rank = newRank;
    }

    @Override
    public Rank average(List<Player> players) {
        List<Rank> rankList = new ArrayList<>();

        for(Player p: players) {
            rankList.add(p.rank);
        }
        Rank firstElem = rankList.remove(0);
        return firstElem.average(rankList);
    }

    public static class PlayerID extends ID {
        public PlayerID(long sciper) {
            super(sciper);
        }
        public PlayerID() {}
    }
}
