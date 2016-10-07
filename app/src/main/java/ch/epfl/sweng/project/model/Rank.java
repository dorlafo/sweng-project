package ch.epfl.sweng.project.model;

/**
 * @author Amaury Combes
 */

public abstract class Rank {
    private final int rank;

    public Rank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}
