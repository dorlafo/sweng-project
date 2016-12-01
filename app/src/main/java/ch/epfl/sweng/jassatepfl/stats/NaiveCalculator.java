package ch.epfl.sweng.jassatepfl.stats;

import ch.epfl.sweng.jassatepfl.model.Rank;

/**
 * @author vincenzobaz
 *         Naive implementation of a rankcalculator which considers the rank to be equal to the
 *         number of won matches.
 */
public class NaiveCalculator extends QuoteCalculator {
    public NaiveCalculator(UserStats stats) {
        super(stats);
    }

    public int computeNewQuote() {
        return getStats().getWonMatches();
    }
}
