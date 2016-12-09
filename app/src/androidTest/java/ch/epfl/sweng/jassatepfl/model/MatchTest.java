package ch.epfl.sweng.jassatepfl.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.alexis;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.amaury;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.colin;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.dorian;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.jimmy;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.marco;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.random;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.threePlayersMatch;
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
    public void testRemovePlayerByIdReturnsFalseWhenNoPlayersToRemove() {
        boolean removed = DummyDataTest.noPlayersMatch().removePlayerById(new Player.PlayerID(12));
        assertFalse(removed);
    }

    @Test
    public void testRemovePlayerByIdRemovesPlayer() {
        Match match = DummyDataTest.threePlayersMatch();
        assertTrue(match.hasParticipant(alexis));
        boolean removed = match.removePlayerById(new Player.PlayerID(245433));
        assertTrue(removed);
        assertFalse(match.hasParticipant(alexis));
    }

    @Test
    public void testRemovePlayerByIdRemovePlayerAndAddSentinelWhenNoPlayerInTeam() {
        Match m = DummyDataTest.twoPlayersMatch();
        assertTrue(m.hasParticipant(vincenzo));
        assertTrue(m.hasParticipant(dorian));
        Map<String, List<String>> teams = m.getTeams();
        assertEquals("SENTINEL", teams.get("Team0").get(0));
        assertEquals("SENTINEL", teams.get("Team1").get(0));

        m.setTeam(0, dorian.getID());
        assertEquals(dorian.getID().toString(), teams.get("Team0").get(0));
        boolean removed = m.removePlayerById(dorian.getID());

        assertTrue(removed);
        assertFalse(m.hasParticipant(dorian));
        teams = m.getTeams();
        assertEquals("SENTINEL", teams.get("Team0").get(0));
        assertEquals("SENTINEL", teams.get("Team1").get(0));
    }

    @Test
    public void getPlayerIndexReturnsCorrectIndexWhenPlayerIsInMatch() {
        Match match = DummyDataTest.fullMatch();
        assertEquals(2, match.getPlayerIndex(new Player.PlayerID("234832")));
    }

    @Test
    public void getPlayerIndexReturnsCorrectIndexWhenPlayerIsNotInMatch() {
        Match match = DummyDataTest.fullMatch();
        assertEquals(-1, match.getPlayerIndex(new Player.PlayerID("123")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTeamThrowsExceptionWhenTeamNumberIsInvalid() {
        Match m = DummyDataTest.fullMatch();
        m.setTeam(3, new Player.PlayerID("234832"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTeamThrowsExceptionWhenPlayerIsNotInMatch() {
        Match m = DummyDataTest.fullMatch();
        m.setTeam(0, new Player.PlayerID("000000"));
    }

    @Test
    public void getTeamsWorks() {
        Match m = DummyDataTest.fullMatch();
        assertEquals(2, m.getTeams().size());
        assertEquals(1, m.getTeams().get("Team0").size());
        assertEquals(1, m.getTeams().get("Team1").size());
        assertEquals("SENTINEL", m.getTeams().get("Team0").get(0));
        assertEquals("SENTINEL", m.getTeams().get("Team1").get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getTeamsReturnsImmutableMap() {
        Match m = DummyDataTest.fullMatch();
        Map<String, List<String>> hm = m.getTeams();
        hm.put("Team42", new ArrayList<>(Collections.singletonList("TEST")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getTeamsReturnsImmutableListInMap() {
        Match m = DummyDataTest.fullMatch();
        Map<String, List<String>> hm = m.getTeams();
        hm.get("Team0").add("TEST");
    }

    @Test
    public void setTeamRemoveSentinelIfOnlyOneMemberIsAssigned() {
        Match m = DummyDataTest.fullMatch();
        m.setTeam(0, new Player.PlayerID("234832"));
        assertEquals("234832", m.getTeams().get("Team0").get(0));
        assertEquals(1, m.getTeams().get("Team0").size());
    }

    @Test
    public void setTeamDoesNotAddDuplicateMemberInOneTeam() {
        Match m = DummyDataTest.fullMatch();
        m.setTeam(0, new Player.PlayerID("234832"));
        m.setTeam(0, new Player.PlayerID("234832"));
        assertEquals("234832", m.getTeams().get("Team0").get(0));
        assertEquals(1, m.getTeams().get("Team0").size());
    }

    @Test
    public void setTeamChangeSwitchPlayerFromTeamCorrectly() {
        Match m = DummyDataTest.fullMatch();
        m.setTeam(0, new Player.PlayerID("234832"));
        m.setTeam(1, new Player.PlayerID("234832"));
        assertEquals("234832", m.getTeams().get("Team1").get(0));
        assertEquals(1, m.getTeams().get("Team1").size());

        assertEquals("SENTINEL", m.getTeams().get("Team0").get(0));
        assertEquals(1, m.getTeams().get("Team0").size());
    }

    @Test
    public void teamAssignmentIsCorrectReturnsTrueWhenAssignmentIsCorrect() {
        Match m = DummyDataTest.fullMatch();
        m.setTeam(0, new Player.PlayerID("234832"));
        m.setTeam(0, new Player.PlayerID("999999"));
        m.setTeam(1, new Player.PlayerID("666666"));
        m.setTeam(1, new Player.PlayerID("249733"));
        assertTrue(m.teamAssignmentIsCorrect());
    }

    @Test
    public void teamAssignmentIsCorrectReturnsFalseWhenATeamContainsASentinel() {
        Match m = DummyDataTest.matchForTest();
        assertFalse(m.teamAssignmentIsCorrect());
    }

    @Test
    public void teamAssignmentIsCorrectReturnsFalseWhenWrongNumberOfPlayerInTeam() {
        Match m = DummyDataTest.fullMatch();
        m.setTeam(0, new Player.PlayerID("234832"));
        m.setTeam(0, new Player.PlayerID("999999"));
        m.setTeam(1, new Player.PlayerID("666666"));
        assertFalse(m.teamAssignmentIsCorrect());
    }

    @Test
    public void setStatusWorksCorrectly() {
        Match m = DummyDataTest.fullMatch();
        assertEquals(Match.MatchStatus.PENDING, m.getMatchStatus());
        m.setStatus(Match.MatchStatus.ACTIVE);
        assertEquals(Match.MatchStatus.ACTIVE, m.getMatchStatus());
    }

    @Test
    public void teamNbForPlayerReturnsMinusOneWhenNoTeamIsAssigned() {
        Match m = DummyDataTest.fullMatch();
        assertEquals(-1, m.teamNbForPlayer(dorian));
    }

    @Test
    public void teamNbForPlayerReturnsMinusOneWhenTheRequestedPlayerIsNotInTheMatch() {
        Match m = DummyDataTest.fullMatch();
        assertEquals(-1, m.teamNbForPlayer(alexis));
    }

    @Test
    public void teamNbForPlayerReturnsCorrectTeamNumber() {
        Match m = DummyDataTest.fullMatch();
        m.setTeam(0, random.getID());
        m.setTeam(1, marco.getID());
        m.setTeam(0, vincenzo.getID());
        m.setTeam(1, dorian.getID());
        assertTrue(m.teamAssignmentIsCorrect());
        assertEquals(0, m.teamNbForPlayer(random));
        assertEquals(1, m.teamNbForPlayer(marco));
        assertEquals(0, m.teamNbForPlayer(vincenzo));
        assertEquals(1, m.teamNbForPlayer(dorian));
    }

    @Test
    public void matchHasChangedReturnFalseIfNotSameMatch() {
        Match m1 = DummyDataTest.fullMatch();
        Match m2 = DummyDataTest.fullMatchWithBob();
        m2.setTime(m1.getTime());

        assertFalse(m1.matchHasChanged(m2) || m2.matchHasChanged(m1));
    }

    @Test
    public void matchHasChangedReturnFalseIfMatchHasNotChanged() {
        Match m1 = DummyDataTest.fullMatch();
        Match m2 = DummyDataTest.fullMatch();
        m2.setTime(m1.getTime());

        assertFalse(m1.matchHasChanged(m2) || m2.matchHasChanged(m1));
    }

    @Test
    public void matchHasChangedReturnTrueIfMatchHasChanged() {
        Match m1 = DummyDataTest.fullMatch();
        Match m2 = DummyDataTest.fullMatch();
        m2.setTime(m1.getTime());

        m1.removePlayerById(DummyDataTest.dorian.getID());
        assertTrue(m1.matchHasChanged(m2) && m2.matchHasChanged(m1));
    }

    @Test
    public void testGetPlayerById() {
        Player player = threePlayersMatch().getPlayerById(colin.getID().toString());
        assertEquals(colin, player);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetPlayerByIdThrowsException() {
        threePlayersMatch().getPlayerById(jimmy.getID().toString());
    }

}
