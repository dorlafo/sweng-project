package ch.epfl.sweng.jassatepfl.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a round object. It contains the points and melds made in the round
 */

public class Round {
    private int points;
    private List<Match.Meld> melds;


    public Round() {
    }

    public Round(int points, List<Match.Meld> melds) {
        this.points = points;
        this.melds = new ArrayList<>(melds);
    }

    /**
     * Getter for the points in this round
     * @return The points made
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter for the meld made in this round
     * @return A list of the meld made
     */
    public List<Match.Meld> getMelds() {
        return Collections.unmodifiableList(melds);
    }

    /**
     * Add the points passed in parameters to the current points
     * @param p The points to be add
     */
    public void addPoints(int p) {
        this.points += p;
    }

    /**
     * Add the meld passed in parameters to the current meld list and the points for this meld to the points
     * @param m The meld to be add
     */
    public void addMeld(Match.Meld m) {
        this.melds.add(m);
        addPoints(m.value());
    }
}
