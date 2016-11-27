package ch.epfl.sweng.jassatepfl.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match.Meld;

import static ch.epfl.sweng.jassatepfl.model.Match.Meld.FOUR_JACKS;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.HUNDRED;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.MARRIAGE;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.THREE_CARDS;
import static junit.framework.Assert.assertEquals;

public final class RoundTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsException() {
        new Round(0);
    }

    @Test
    public void testTeamCountGetter() {
        Round round = new Round(2);
        assertEquals(2, round.getTeamCount());
    }

    @Test
    public void testScoresGetter() {
        Round round = new Round(2);
        updateScore(round, 0, 25);
        updateScore(round, 1, 37);
        Map<String, Integer> map = new HashMap<>();
        map.put("TEAM0", 25);
        map.put("TEAM1", 37);
        assertEquals(map, round.getScores());
    }

    @Test
    public void testMeldsGetter() {
        Round round = new Round(1);
        addMeld(round, 0, MARRIAGE);
        addMeld(round, 0, THREE_CARDS);
        List<Meld> melds = new ArrayList<>();
        melds.add(MARRIAGE);
        melds.add(THREE_CARDS);
        Map<String, List<Meld>> map = new HashMap<>();
        map.put("TEAM0", melds);
        assertEquals(map, round.getMelds());
    }

    @Test
    public void testMeldScoreGetter() {
        Round round = new Round(1);
        addMeld(round, 0, MARRIAGE);
        addMeld(round, 0, THREE_CARDS);
        Map<String, Integer> map = new HashMap<>();
        map.put("TEAM0", 40);
        assertEquals(map, round.getMeldScores());
    }

    @Test
    public void testTeamScoreGetter() {
        Round round = new Round(2);
        updateScore(round, 0, 25);
        updateScore(round, 1, 52);
        assertEquals(Integer.valueOf(25), round.getTeamCardScore(0));
        assertEquals(Integer.valueOf(52), round.getTeamCardScore(1));
    }

    @Test
    public void testTeamMeldScoreGetter() {
        Round round = new Round(2);
        addMeld(round, 0, HUNDRED);
        addMeld(round, 0, FOUR_JACKS);
        assertEquals(Integer.valueOf(300), round.getTeamMeldScore(0));
    }

    @Test
    public void testTotalRoundScore() {
        Round round = new Round(1);
        updateScore(round, 0, 25);
        addMeld(round, 0, FOUR_JACKS);
        assertEquals(Integer.valueOf(225), round.getTeamTotalScore(0));
    }

    @Test
    public void testTeamMeldGetter() {
        Round round = new Round(1);
        addMeld(round, 0, MARRIAGE);
        addMeld(round, 0, THREE_CARDS);
        List<Meld> melds = new ArrayList<>();
        melds.add(MARRIAGE);
        melds.add(THREE_CARDS);
        assertEquals(melds, round.getTeamMelds(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTeamCardScoreGetterThrowsException() {
        Round round = new Round(1);
        round.getTeamCardScore(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTeamMeldScoreGetterThrowsException() {
        Round round = new Round(1);
        round.getTeamMeldScore(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTeamTotalScoreGetterThrowsException() {
        Round round = new Round(1);
        round.getTeamTotalScore(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTeamMeldsGetterThrowsException() {
        Round round = new Round(1);
        round.getTeamMelds(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetTeamScoreThrowsException() {
        Round round = new Round(1);
        round.setTeamScore(2, 12);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddMeldThrowsException() {
        Round round = new Round(1);
        round.addMeldToTeam(2, FOUR_JACKS);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCancelMeldThrowsException() {
        Round round = new Round(1);
        round.cancelLastMeld(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMeldToStringThrowsException() {
        Round round = new Round(1);
        round.meldsToString(-1);
    }

    @Test
    public void testMeldToStringNoMelds() {
        Round round = new Round(1);
        assertEquals("-", round.meldsToString(0));
    }

    @Test
    public void testMeldToStringOneMeld() {
        Round round = new Round(1);
        addMeld(round, 0, THREE_CARDS);
        assertEquals(THREE_CARDS.toString(), round.meldsToString(0));
    }

    @Test
    public void testMeldToStringTwoMelds() {
        Round round = new Round(1);
        addMeld(round, 0, THREE_CARDS);
        addMeld(round, 0, FOUR_JACKS);
        String expected = THREE_CARDS.toString() + ", " + FOUR_JACKS.toString();
        assertEquals(expected, round.meldsToString(0));
    }

    private void updateScore(Round round, int teamIndex, int score) {
        round.setTeamScore(teamIndex, score);
    }

    private void addMeld(Round round, int teamIndex, Meld meld) {
        round.addMeldToTeam(teamIndex, meld);
    }

}
