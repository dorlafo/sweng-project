package ch.epfl.sweng.project.model;

/**
 * Rank is class that represents a rank in a competition system.
 * It implements the Comparable interface
 */
public class Rank implements Comparable<Rank> {
    private int rank;

    /**
     * Default constructor required for calls to DataSnapshot.getValue when using Firebase
     */
    public Rank() {

    }

    /**
     * Constructor of the Rank class
     *
     * @param rank the value of the rank
     */
    public Rank(int rank) {
        this.rank = rank;
    }

    /**
     * Provides a string representation for the Rank class
     *
     * @return returns a string representation of the Rank class
     */
    public String toSring() {
        return Integer.toString(rank);
    }

    public Rank add(Rank other) {
        return new Rank(this.rank + other.rank);
    }

    @Override
    public int compareTo(Rank o) {
        return ((Integer) rank).compareTo(o.rank);
    }

    public int getRank() {
        return this.rank;
    }
}
