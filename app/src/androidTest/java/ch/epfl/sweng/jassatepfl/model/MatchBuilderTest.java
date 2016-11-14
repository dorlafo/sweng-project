package ch.epfl.sweng.jassatepfl.model;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.epfl.sweng.jassatepfl.model.Player.PlayerID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public final class MatchBuilderTest {

    private Match.Builder matchBuilder;
    private final Player amaury = new Player(new PlayerID(1), "Combes", "Amaury", new Rank(123));
    private final Player vincenzo = new Player(new PlayerID(2), "Bazzucchi", "Vincenzo", new Rank(345));
    private final Player dorian = new Player(new PlayerID(3), "Laforest", "Dorian", new Rank(567));
    private final Player alexis = new Player(new PlayerID(4), "Montavon", "Alexis", new Rank(789));
    private final Player random = new Player(new PlayerID(5), "Smith", "John", new Rank(7));

    public void setUp() {
        matchBuilder = new Match.Builder();
    }

    @Test
    public void defaultMatchBuilderHasCorrectValues() {
        setUp();
        try {
            matchBuilder.addPlayer(amaury);
        } catch (IllegalStateException | IllegalAccessException e) {
            fail();
        }
        Match match = matchBuilder.build();

        List<Player> players = new ArrayList<>();
        players.add(amaury);
        Match defaultMatch = new Match(players, new GPSPoint(46.520407, 6.565802),
                Match.Builder.DEFAULT_DESCRIPTION, false, Calendar.getInstance().getTimeInMillis() + 2 * 3600 * 1000,
                Match.Builder.DEFAULT_ID);

        assertEquals(defaultMatch, match);
    }

    @Test
    public void builderSetsFieldsCorrectly() {
        setUp();
        try {
            matchBuilder.addPlayer(amaury);
        } catch (IllegalStateException | IllegalAccessException e) {
            fail();
        }


        GPSPoint newLocation = new GPSPoint(33.02245, 15.04457);
        String newDescription = "This is gonna be a great match!";
        long newExpirationTime = Calendar.getInstance().getTimeInMillis()
                + 24 * 3600 * 1000;

        matchBuilder.setLocation(newLocation).setDescription(newDescription)
                .setPrivacy(true).setExpirationTime(newExpirationTime)
                .setMatchID("new match id");

        Match setMatch = matchBuilder.build();

        assertEquals(newLocation, setMatch.getLocation());
        assertEquals(newDescription, setMatch.getDescription());
        assertTrue(setMatch.isPrivateMatch());
        assertEquals(4, setMatch.getMaxPlayerNumber());
        assertEquals(newExpirationTime, setMatch.getExpirationTime(), 10);
        assertEquals("new match id", setMatch.getMatchID());
    }

    @Test
    public void buildingWithEmptyPlayerListThrowsException() {
        setUp();
        try {
            matchBuilder.build();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().equals("Cannot create match without any player."));
        }
    }

    @Test
    public void builderCorrectlyAddsPlayers() {
        setUp();
        try {
            matchBuilder.addPlayer(amaury);
            matchBuilder.addPlayer(vincenzo);
        } catch (IllegalStateException | IllegalAccessException e) {
            fail();
        }

        Match match = matchBuilder.build();

        Player player1 = match.getPlayers().get(0);
        Player player2 = match.getPlayers().get(1);

        assertEquals(amaury, player1);
        assertEquals(vincenzo, player2);
    }

    @Test
    public void builderDoesNotAddTheSamePlayerTwice() {
        setUp();
        try {
            matchBuilder.addPlayer(amaury).addPlayer(amaury)
                    .addPlayer(vincenzo).addPlayer(vincenzo);
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalAccessException a) {
            assertTrue(a.getMessage().equals("Player already in that Match."));
        }
    }

    @Test
    public void addingTooManyPlayersThrowsException() {
        setUp();
        try {
            matchBuilder.addPlayer(amaury).addPlayer(vincenzo)
                    .addPlayer(dorian).addPlayer(alexis);
        } catch (IllegalStateException | IllegalAccessException e) {
            fail();
        }

        try {
            matchBuilder.addPlayer(random);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().equals("Match is full."));
        } catch (IllegalAccessException a) {
            fail();
        }
    }

    @Test
    public void testRemovingFromEmptyListThrowsException() {
        setUp();
        try {
            matchBuilder.removePlayer(amaury);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().equals("No players in the match."));
        }
    }

    @Test
    public void testRemovingPlayerNotInMatchThrowsException() {
        setUp();
        try {
            matchBuilder.addPlayer(amaury);
            matchBuilder.removePlayer(vincenzo);
            fail("Expected IllegalArgumentException");
        } catch (IllegalStateException | IllegalAccessException e) {
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("Player not in the Match."));
        }
    }

    @Test
    public void testRemovePlayerIsCorrect() {
        setUp();
        try {
            matchBuilder.addPlayer(amaury);
            matchBuilder.removePlayer(amaury);
        } catch (IllegalStateException | IllegalArgumentException | IllegalAccessException e) {
            fail();
        }
        assertTrue(matchBuilder.getPlayerList().isEmpty());
    }

}
