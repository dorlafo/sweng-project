package ch.epfl.sweng.jassatepfl.stats.trueskill;


import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import java.util.Objects;

/**
 * Class that represents a rank in a competition system.
 * It implements the Comparable interface.
 */
public class Rank {
    private Double mean;
    private Double standardDeviation;

    /**
     * Default constructor required for calls to DataSnapshot.getValue when using Firebase
     */
    public Rank() {

    }

    /**
     * Constructs a new Rank with the given value.
     *
     * @param mean The value of the rank
     */
    public Rank(double mean) {
        this.mean = mean;
    }

    /**
     * Construct a new rank with the mean and standardDeviation given
     * @param mean
     * @param standardDeviation
     */
    public Rank(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public void copy(Rank rank) {
        mean = rank.getMean();
        standardDeviation = rank.getStandardDeviation();
    }
    /**
     * Getter for the rank.
     *
     * @return The rank value
     */
    public Double getMean() {
        return mean;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public int getRank() {
        return (int) Math.ceil((mean - 3 * standardDeviation) * 10 + 1000);
    }

    public static Rank getDefaultRank() {
        return new Rank(GameInfo.defaultInitialMean, GameInfo.defaultInitialStandardDeviation);
    }

    /*
    @Override
    public int compareTo(@NonNull Rank o) {
        return ((Integer) rank).compareTo(o.rank);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        return this.getRank() == ((Rank) other).getRank();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(this.getRank());
    }

    /**
     * Computes and returns the sum of the current Rank and another.
     *
     * @param other The Rank to add to the current Rank
     * @return A new Rank with value equal to the sum of the other two
     */

    /*
    public Rank add(Rank other) {
        return new Rank(this.rank + other.rank);
    }
    */

}
