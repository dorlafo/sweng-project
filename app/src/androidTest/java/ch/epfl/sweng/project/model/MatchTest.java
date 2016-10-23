package ch.epfl.sweng.project.model;


import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

public final class MatchTest {

    public Match.Builder defaultBuilder() {
        Player amaury = new Player(new Player.PlayerID(1), "Combes", "Amaury", new Rank(123));
        Player vincenzo = new Player(new Player.PlayerID(2), "Bazzucchi", "Vincenzo", new Rank(345));

        return new Match.Builder().addPlayer(amaury).addPlayer(vincenzo);
    }

    @Test
    public void equalsReturnsFalseWhenComparingWithNull() {
        Match match = defaultBuilder().build();
        assertFalse(match.equals(null));
    }

    @Test
    public void equalsReturnsFalseWhenComparingWithOtherClass() {
        Match match = defaultBuilder().build();
        assertFalse(match.equals(new ArrayList<Player>()));
    }

    @Test
    public void equalsReturnsFalseWhenComparingDifferentMatches() {
        Match match1 = defaultBuilder().build();
        Match match2 = defaultBuilder().setDescription("Hello").setPrivacy(true)
                .setLocation(new GPSPoint(45.1234, 15.6789)).build();

        assertFalse(match1.equals(match2) || match2.equals(match1));
    }

    @Test
    public void equalsIsReflexive() {
        Match match = defaultBuilder().build();
        assertTrue(match.equals(match));
    }

    @Test
    public void equalsReturnsTrueWhenComparingSamePlayers() {
        Match match1 = defaultBuilder().build();
        Match match2 = defaultBuilder().build();

        assertNotSame(match1, match2);
        assertTrue(match1.equals(match2) && match2.equals(match1));
    }

    @Test
    public void hashCodeIsTheSameForSameMatches() {
        Match match1 = defaultBuilder().build();
        Match match2 = defaultBuilder().build();

        assertNotSame(match1, match2);
        assertEquals(match1, match2);
        assertEquals(match1.hashCode(), match2.hashCode());
    }

    @Test
    public void hashCodeIsDifferentForDifferentMatches() {
        Match match1 = defaultBuilder().build();
        Match match2 = defaultBuilder().setDescription("Hello").setPrivacy(true)
                .setLocation(new GPSPoint(45.1234, 15.6789)).build();

        assertNotEquals(match1.hashCode(), match2.hashCode());
    }

}
