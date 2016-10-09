package ch.epfl.sweng.project.model;

/**
 * @author Amaury Combes
 */

public abstract class Rank implements Comparable<Rank> {
    private int rank;

    public Rank() { }
    public Rank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public int compareTo(Rank o) {
        return  ((Integer) rank).compareTo(o.getRank());
    }
}
