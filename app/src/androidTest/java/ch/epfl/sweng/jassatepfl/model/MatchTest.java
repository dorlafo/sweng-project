package ch.epfl.sweng.jassatepfl.model;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.alexis;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.amaury;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.dorian;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.random;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.vincenzo;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotEquals;

@SuppressWarnings({"EqualsBetweenInconvertibleTypes", "EqualsWithItself", "ObjectEqualsNull"})
public final class MatchTest {

    public Match.Builder defaultBuilder() {
        Match.Builder match = null;
        try {
            match = new Match.Builder().addPlayer(amaury).addPlayer(vincenzo);
        } catch (IllegalStateException | IllegalAccessException e) {
            fail();
        }
        return match;
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
        Match match1 = defaultBuilder().setMatchID("match1").build();
        Match match2 = defaultBuilder().setMatchID("match2").setDescription("Hello")
                .setPrivacy(true).setLocation(new GPSPoint(45.1234, 15.6789)).build();

        assertFalse(match1.equals(match2) || match2.equals(match1));
    }

    @Test
    public void equalsIsReflexive() {
        Match match = defaultBuilder().build();
        assertTrue(match.equals(match));
    }

    @Test
    public void equalsReturnsTrueWhenComparingSameMatches() {
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
                .setMatchID("second match").setLocation(new GPSPoint(45.1234, 15.6789)).build();

        assertNotEquals(match1.hashCode(), match2.hashCode());
    }

    @Test
    public void matchReturnsItsCreator() {
        Match match = defaultBuilder().build();
        assertEquals(amaury, match.createdBy());
    }

    @Test
    public void matchAddsPlayer() {
        Match match = defaultBuilder().build();
        try {
            match.addPlayer(alexis);
        } catch (IllegalStateException | IllegalAccessException e) {
            fail();
        }
        assertEquals(3, match.getPlayers().size());
        assertEquals(alexis, match.getPlayers().get(2));
    }

    @Test
    public void matchDoesNotAddTheSamePlayerTwice() {
        Match match = defaultBuilder().build();

        try {
            match.addPlayer(alexis);
            match.addPlayer(vincenzo);
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalAccessException a) {
            assertEquals("Player already in that Match.", a.getMessage());
        }

        assertEquals(3, match.getPlayers().size());
        assertNotEquals(vincenzo, match.getPlayers().get(2));
        assertEquals(alexis, match.getPlayers().get(2));
    }

    @Test
    public void addingTooManyPlayersThrowsException() {
        Match match = defaultBuilder().build();
        try {
            match.addPlayer(alexis);
            match.addPlayer(dorian);
        } catch (IllegalStateException | IllegalAccessException e) {
            fail();
        }

        try {
            match.addPlayer(random);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Match is full.", e.getMessage());
        } catch (IllegalAccessException a) {
            fail();
        }
    }

    @Test
    public void hasParticipantReturnsTrueIfPlayerIsInMatch() {
        Match match = defaultBuilder().build();
        assertTrue(match.hasParticipant(vincenzo));
        assertTrue(match.hasParticipant(amaury));
    }

    @Test
    public void hasParticipantReturnsFalseIfPlayerIsNotInMatch() {
        Match match = defaultBuilder().build();
        assertFalse(match.hasParticipant(null));
        assertFalse(match.hasParticipant(dorian));
    }

    @Test
    public void testRemovePlayerReturnsFalseWhenNoPlayersToRemove() {
        boolean removed = DummyDataTest.noPlayersMatch().removePlayerById(new Player.PlayerID(12));
        assertFalse(removed);
    }

    @Test
    public void testRemovePlayerById() {
        Match match = DummyDataTest.threePlayersMatch();
        assertTrue(match.hasParticipant(alexis));
        boolean removed = match.removePlayerById(new Player.PlayerID(245433));
        assertTrue(removed);
        assertFalse(match.hasParticipant(alexis));
    }

}
