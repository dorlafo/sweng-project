package ch.epfl.sweng.jassatepfl.stats;

import org.junit.Test;

import static ch.epfl.sweng.jassatepfl.model.Match.GameVariant.CHIBRE;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.FOUR_JACKS;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.LAST_TRICK;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.THREE_CARDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for MatchStats class
 */
public class MatchStatsTest {

    @Test
    public void testTotalScoreGetter() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        stats.setMeld(0, LAST_TRICK);
        setTeamScores(stats, 57, 100);
        stats.setMeld(1, FOUR_JACKS);
        setTeamScores(stats, 33, 122);
        assertEquals(Integer.valueOf(95), stats.getTotalMatchScore(0));
        assertEquals(Integer.valueOf(422), stats.getTotalMatchScore(1));
    }

    @Test
    public void testCurrentRoundScoreGetter() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        stats.setScore(0, 89);
        stats.setMeld(0, THREE_CARDS);
        stats.setScore(1, 68);
        assertEquals(Integer.valueOf(109), stats.getCurrentRoundTeamScore(0));
        assertEquals(Integer.valueOf(68), stats.getCurrentRoundTeamScore(1));
    }

    @Test
    public void testCancelLastRound() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        setTeamScores(stats, 57, 100);
        setTeamScores(stats, 100, 57);
        assertEquals(Integer.valueOf(157), stats.getTotalMatchScore(0));
        assertEquals(Integer.valueOf(157), stats.getTotalMatchScore(1));
        stats.cancelLastRound(0);
        assertEquals(Integer.valueOf(57), stats.getTotalMatchScore(0));
        assertEquals(Integer.valueOf(100), stats.getTotalMatchScore(1));
    }

    @Test
    public void testMatchIsOver() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        assertFalse(stats.goalHasBeenReached());
        setTeamScores(stats, 500, 1000);
        assertTrue(stats.goalHasBeenReached());
        assertEquals(1, stats.getWinnerIndex());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRoundScoreGetterThrowsException() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        stats.getCurrentRoundTeamScore(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMatchScoreGetterThrowsException() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        stats.getTotalMatchScore(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetScoreThrowsException() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        stats.setScore(2, 50);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetMeldThrowsException() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        stats.setMeld(2, FOUR_JACKS);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCancelThrowsExceptionForInvalidIndex() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        stats.cancelLastRound(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelThrowsExceptionWhenNothingToCancel() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        stats.cancelLastRound(0);
    }

    @Test
    public void testAllTeamsHaveReachedGoalReturnsTrue() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        setTeamScores(stats, 1020, 1050);
        assertTrue(stats.allTeamsHaveReachedGoal());
    }

    @Test
    public void testAllTeamsHaveReachedGoalReturnsFalse() {
        MatchStats stats = new MatchStats("hello", CHIBRE);
        setTeamScores(stats, 1020, 500);
        assertFalse(stats.allTeamsHaveReachedGoal());
    }

    private void setTeamScores(MatchStats stats, int firstTeamScore, int secondTeamScore) {
        stats.setScore(0, firstTeamScore);
        stats.setScore(1, secondTeamScore);
        stats.finishRound();
    }

}
