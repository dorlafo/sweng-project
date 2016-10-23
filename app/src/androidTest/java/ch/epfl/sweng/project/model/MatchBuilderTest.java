package ch.epfl.sweng.project.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.epfl.sweng.project.model.Player.PlayerID;

import static ch.epfl.sweng.project.model.Match.GameVariant.CLASSIC;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotEquals;

public final class MatchBuilderTest {
    private Match.Builder matchBuilder;
    private Player amaury = new Player(new PlayerID(1), "Combes", "Amaury", new Rank(123));
    private Player vincenzo = new Player(new PlayerID(2), "Bazzucchi", "Vincenzo", new Rank(345));
    private Player dorian = new Player(new PlayerID(3), "Laforest", "Dorian", new Rank(567));
    private Player alexis = new Player(new PlayerID(4), "Montavon", "Alexis", new Rank(789));
    private Player random = new Player(new PlayerID(5), "Smith", "John", new Rank(7));

    public void setUp() {
        matchBuilder = new Match.Builder();
    }

    @Test
    public void defaultMatchBuilderHasCorrectValues() {
        setUp();
        matchBuilder.addPlayer(amaury);
        Match match = matchBuilder.build();

        List<Player> players = new ArrayList<>();
        players.add(amaury);
        Match defaultMatch = new Match(players, new GPSPoint(46.520407, 6.565802),
                Match.Builder.DEFAULT_DESCRIPTION, false, CLASSIC,
                Calendar.getInstance().getTimeInMillis() + 2 * 3600 * 1000);

        assertEquals(defaultMatch, match);

        /*
        assertFalse(match.getPlayers().isEmpty());
        assertEquals(46.520407, match.getLocation().getLatitude());
        assertEquals(6.565802, match.getLocation().getLongitude());
        assertEquals(Match.Builder.DEFAULT_DESCRIPTION, match.getDescription());
        assertEquals(123, match.getRank().getRank());
        assertFalse(match.isPrivateMatch());
        assertEquals(CLASSIC, match.getGameVariant());
        assertEquals(4, match.getMaxPlayerNumber());
        assertEquals(Calendar.getInstance().getTimeInMillis() + 2 * 3600 * 1000,
                match.getExpirationTime(), 10);
        */
    }

    @Test
    public void builderSetsFieldsCorrectly() {
        setUp();
        matchBuilder.addPlayer(amaury);

        GPSPoint newLocation = new GPSPoint(33.02245, 15.04457);
        String newDescription = "This is gonna be a great match!";
        long newExpirationTime = Calendar.getInstance().getTimeInMillis()
                + 24 * 3600 * 1000;

        matchBuilder.setLocation(newLocation).setDescription(newDescription)
                .setPrivacy(true).setExpirationTime(newExpirationTime);

        Match setMatch = matchBuilder.build();

        assertEquals(newLocation, setMatch.getLocation());
        assertEquals(newDescription, setMatch.getDescription());
        assertTrue(setMatch.isPrivateMatch());
        assertEquals(newExpirationTime, setMatch.getExpirationTime(), 10);
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
        matchBuilder.addPlayer(amaury);
        matchBuilder.addPlayer(vincenzo);
        Match match = matchBuilder.build();

        Player player1 = match.getPlayers().get(0);
        Player player2 = match.getPlayers().get(1);

        assertEquals(amaury, player1);
        assertEquals(vincenzo, player2);
    }

    @Test
    public void builderDoesNotAddTheSamePlayerTwice() {
        setUp();
        matchBuilder.addPlayer(amaury).addPlayer(amaury)
                .addPlayer(vincenzo).addPlayer(vincenzo);
        Match match = matchBuilder.build();

        Player player1 = match.getPlayers().get(0);
        Player player2 = match.getPlayers().get(1);

        assertEquals(2, match.getPlayers().size());
        assertEquals(amaury, player1);
        assertNotEquals(amaury, player2);
        assertEquals(vincenzo, player2);
    }

    @Test
    public void addingTooManyPlayersThrowsException() {
        setUp();
        matchBuilder.addPlayer(amaury).addPlayer(vincenzo)
                .addPlayer(dorian).addPlayer(alexis);
        try {
            matchBuilder.addPlayer(random);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().equals("Match is full."));
        }
    }

}
