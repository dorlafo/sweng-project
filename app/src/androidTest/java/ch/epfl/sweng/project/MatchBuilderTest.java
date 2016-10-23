package ch.epfl.sweng.project;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Calendar;

import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.model.Player.PlayerID;
import ch.epfl.sweng.project.model.Rank;

import static ch.epfl.sweng.project.model.Match.GameVariant.CLASSIC;
import static junit.framework.Assert.fail;

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

        Assert.assertFalse(match.getPlayers().isEmpty());
        Assert.assertEquals(46.520407, match.getLocation().getLatitude());
        Assert.assertEquals(6.565802, match.getLocation().getLongitude());
        Assert.assertEquals("New Match", match.getDescription());
        Assert.assertEquals(123, match.getRank().getRank());
        Assert.assertFalse(match.isPrivateMatch());
        Assert.assertEquals(CLASSIC, match.getGameVariant());
        Assert.assertEquals(4, match.getMaxPlayerNumber());
        Assert.assertEquals(Calendar.getInstance().getTimeInMillis() + 2 * 3600 * 1000,
                match.getExpirationTime(), 10);
    }

    @Test
    public void buildingWithEmptyPlayerListThrowsException() {
        setUp();
        try {
            matchBuilder.build();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().equals("Cannot create match without any player."));
        }

    }

    @Test
    public void builderCorrectlyAddsPlayer() {
        setUp();
        matchBuilder.addPlayer(amaury);
        Match match = matchBuilder.build();

        Player player = match.getPlayers().get(0);

        Assert.assertEquals("Amaury", player.getFirstName());
        Assert.assertEquals("Combes", player.getLastName());
    }

    @Test
    public void buildingWithTooManyPlayersThrowsException() {
        setUp();
        matchBuilder.addPlayer(amaury).addPlayer(vincenzo)
                .addPlayer(dorian).addPlayer(alexis);
        try {
            matchBuilder.addPlayer(random);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().equals("Match is full."));
        }

    }

}
