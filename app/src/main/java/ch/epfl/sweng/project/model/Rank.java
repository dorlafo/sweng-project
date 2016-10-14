package ch.epfl.sweng.project.model;

import java.util.List;


public class Rank implements Averageable<Rank, Rank>, Comparable<Rank> {
    private int rank;

    public Rank() {

    }

    public Rank(int rank) {
        this.rank = rank;
    }

    public String toSring() {
        return Integer.toString(rank);
    }

    @Override
    public Rank average(List<Rank> ranks) {
        int sumRanks = this.rank;
        int numElem = 1;

        for(Rank r: ranks) {
            sumRanks += r.rank;
            ++numElem;
        }

        return new Rank(sumRanks);
    }

    @Override
    public int compareTo(Rank o) {
        return  ((Integer) rank).compareTo(o.rank);
    }
}
