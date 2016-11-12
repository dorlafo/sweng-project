package ch.epfl.sweng.jassatepfl.stats;

import org.junit.Test;

import ch.epfl.sweng.jassatepfl.model.Rank;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by vinz on 11/12/16.
 */
public class NaiveCalculatorTest {
    NaiveCalculator nv = new NaiveCalculator(UserStatsTest.stats);

    @Test
    public void computeNewRank() throws Exception {
        assertThat(nv.computeNewRank().equals(new Rank(1)), is(true));
    }

}