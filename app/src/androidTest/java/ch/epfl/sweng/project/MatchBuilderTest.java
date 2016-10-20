package ch.epfl.sweng.project;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Calendar;

import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.model.Player.PlayerID;
import ch.epfl.sweng.project.model.Rank;

import static ch.epfl.sweng.project.model.Match.GameVariant.CLASSIC;

public final class MatchBuilderTest {
    private Match.Builder matchBuilder;

    public void setUp() {
        matchBuilder = new Match.Builder();
    }

    @Test
    public void defaultMatchBuilderHasCorrectValues() {
        setUp();
        matchBuilder.addPlayer(new Player(new PlayerID(1), "Combes", "Amaury", new Rank(123)));
        Match match = matchBuilder.build();

        Assert.assertFalse(match.getPlayers().isEmpty());
        Assert.assertEquals(46.520407, match.getLocation().getLatitude());
        Assert.assertEquals(6.565802, match.getLocation().getLatitude());
        Assert.assertEquals("New Match", match.getDescription());
        Assert.assertEquals(0, match.getRank());
        Assert.assertFalse(match.isPrivateMatch());
        Assert.assertEquals(CLASSIC, match.getGameVariant());
        Assert.assertEquals(4, match.getMaxPlayerNumber());
        Assert.assertEquals(Calendar.getInstance().getTimeInMillis() + 2 * 3600 * 1000,
                match.getExpirationTime(), 60);
    }

    @Test
    public void builderCorrectlyAddsPlayer() {
        setUp();
        matchBuilder.addPlayer(new Player(new PlayerID(1), "Combes", "Amaury", new Rank(123)));
        Match match = matchBuilder.build();

        Assert.assertEquals("Amaury Combes", match.getPlayers().get(0).toString());
    }

}
