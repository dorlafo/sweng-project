package ch.epfl.sweng.jassatepfl.model;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.alexis;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.amaury;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.dorian;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.random;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.vincenzo;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class MatchBuilderTest {

    private Match.Builder matchBuilder;

    @Before
    public void setUp() {
        matchBuilder = new Match.Builder();
    }

    @Test
    public void defaultMatchBuilderHasCorrectValues() throws IllegalAccessException {
        matchBuilder.addPlayer(amaury);
        Match match = matchBuilder.build();

        List<Player> players = new ArrayList<>();
        Map<String, Boolean> hasCards = new HashMap<>();
        players.add(amaury);
        Match defaultMatch = new Match(players, new GPSPoint(46.520407, 6.565802),
                Match.Builder.DEFAULT_DESCRIPTION, false, Calendar.getInstance().getTimeInMillis() + 2 * 3600 * 1000,
                Match.Builder.DEFAULT_ID, hasCards);

        assertEquals(defaultMatch, match);
    }

    @Test
    public void builderSetsFieldsCorrectly() throws IllegalAccessException {
        matchBuilder.addPlayer(amaury);

        GPSPoint newLocation = new GPSPoint(33.02245, 15.04457);
        String newDescription = "This is gonna be a great match!";
        long newExpirationTime = Calendar.getInstance().getTimeInMillis()
                + 24 * 3600 * 1000;

        matchBuilder.setLocation(newLocation).setDescription(newDescription)
                .setPrivacy(true).setTime(newExpirationTime)
                .setMatchID("new match id");

        Match setMatch = matchBuilder.build();

        assertEquals(newLocation, setMatch.getLocation());
        assertEquals(newDescription, setMatch.getDescription());
        assertTrue(setMatch.isPrivateMatch());
        assertEquals(4, setMatch.getMaxPlayerNumber());
        assertEquals(newExpirationTime, setMatch.getTime(), 10);
        assertEquals("new match id", setMatch.getMatchID());
        assertTrue(!matchBuilder.getHasCards().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void buildingWithEmptyPlayerListThrowsException() {
        matchBuilder.build();
    }

    @Test
    public void builderCorrectlyAddsPlayers() throws IllegalAccessException {
        matchBuilder.addPlayer(amaury);
        matchBuilder.addPlayer(vincenzo);
        Match match = matchBuilder.build();

        Player player1 = match.getPlayers().get(0);
        Player player2 = match.getPlayers().get(1);

        assertEquals(amaury, player1);
        assertEquals(vincenzo, player2);
    }

    @Test(expected = IllegalAccessException.class)
    public void builderDoesNotAddTheSamePlayerTwice() throws IllegalAccessException {
        matchBuilder.addPlayer(amaury).addPlayer(amaury)
                .addPlayer(vincenzo).addPlayer(vincenzo);
    }

    @Test(expected = IllegalStateException.class)
    public void addingTooManyPlayersThrowsException() throws IllegalAccessException {
        matchBuilder.addPlayer(amaury).addPlayer(vincenzo)
                .addPlayer(dorian).addPlayer(alexis);
        matchBuilder.addPlayer(random);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemovingFromEmptyListThrowsException() {
        matchBuilder.removePlayer(amaury);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemovingPlayerNotInMatchThrowsException() throws IllegalAccessException {
        matchBuilder.addPlayer(amaury);
        matchBuilder.removePlayer(vincenzo);
    }

    @Test
    public void testRemovePlayerIsCorrect() throws IllegalAccessException {
        matchBuilder.addPlayer(amaury);
        matchBuilder.removePlayer(amaury);
        assertTrue(matchBuilder.getPlayerList().isEmpty());
    }

    @Test
    public void setStatusWorksCorrectly() {
        matchBuilder.setStatus(Match.MatchStatus.ACTIVE);
        assertEquals(Match.MatchStatus.ACTIVE, matchBuilder.getMatchStatus());
    }

}
