package ch.epfl.sweng.project.model;

import java.util.List;

/**
 * Rank is class that represents a rank in a competition system.
 * It implements the Averageable interface and the Comparable interface
 */
public class Rank implements Averageable<Rank, Rank>, Comparable<Rank> {
    private int rank;

    /**
     * Default constructor required for calls to DataSnapshot.getValue when using Firebase
     */
    public Rank() {

    }

    /**
     * Constructor of the Rank class
     * @param rank the value of the rank
     */
    public Rank(int rank) {
        this.rank = rank;
    }

    /**
     * Provides a string representation for the Rank class
     * @return returns a string representation of the Rank class
     */
    public String toSring() {
        return Integer.toString(rank);
    }

    @Override
    public Rank average(List<Rank> ranks) {
        int sumRanks = rank;
        int numElem = 1;

        for(Rank r: ranks) {
            sumRanks += r.rank;
            ++numElem;
        }

        return new Rank(sumRanks / numElem);
    }

    @Override
    public int compareTo(Rank o) {
        return  ((Integer) rank).compareTo(o.rank);
    }
}
