package ch.epfl.sweng.jassatepfl.stats;

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
