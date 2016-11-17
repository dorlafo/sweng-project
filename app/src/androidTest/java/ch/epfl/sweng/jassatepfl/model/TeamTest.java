package ch.epfl.sweng.jassatepfl.model;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Team class
 */
public class TeamTest {
    @Test
    public void teamIsImmutable() {
        Player.PlayerID p1 = new Player.PlayerID(234832);
        Player.PlayerID p2 = new Player.PlayerID(244532);
        Set<Player.PlayerID> players = new HashSet<>();
        players.add(p1);
        players.add(p2);

        Team t = new Team(players);
        players.clear();

        assertThat(t.getNumberOfMembers(), is(2));
        assertThat(t.getMembers().size(), is(2));
        assertThat(t.getMembers().contains(p1), is(true));
        assertThat(t.getMembers().contains(p2), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyTeamCannotBeCreated() {
        Set<Player.PlayerID> p = Collections.emptySet();
        Team t = new Team(p);
    }

    @Test
    public void equalsReturnsFalseWhenComparingWithNull() {
        Player.PlayerID p1 = new Player.PlayerID(234832);
        Player.PlayerID p2 = new Player.PlayerID(244532);
        Set<Player.PlayerID> players = new HashSet<>();
        players.add(p1);
        players.add(p2);

        Team t = new Team(players);

        assertThat(t.equals(null), is(false));
    }

    @Test
    public void equalsReturnsFalseWhenComparingWithOtherClass() {
        Long randomLong = Long.valueOf("78545465465461");
        Player.PlayerID p1 = new Player.PlayerID(234832);
        Player.PlayerID p2 = new Player.PlayerID(244532);
        Set<Player.PlayerID> players = new HashSet<>();
        players.add(p1);
        players.add(p2);

        Team t = new Team(players);

        assertThat(t.equals(randomLong), is(false));
    }

    @Test
    public void equalsReturnsFalseWhenComparingDifferentTeams() {
        Player.PlayerID p1 = new Player.PlayerID(234832);
        Player.PlayerID p2 = new Player.PlayerID(244532);
        Set<Player.PlayerID> players = new HashSet<>();
        players.add(p1);
        Team t1 = new Team(players);
        players.add(p2);
        Team t2 = new Team(players);

        assertThat(t1.equals(t2) || t2.equals(t1), is(false));
    }

    @Test
    public void equalsIsReflexive() {
        Player.PlayerID p1 = new Player.PlayerID(234832);
        Player.PlayerID p2 = new Player.PlayerID(244532);
        Set<Player.PlayerID> players = new HashSet<>();
        players.add(p1);
        players.add(p2);
        Team t = new Team(players);

        assertThat(t.equals(t), is(true));
    }

    @Test
    public void equalsReturnsTrueWhenComparingSameTeams() {
        Player.PlayerID p1 = new Player.PlayerID(234832);
        Player.PlayerID p2 = new Player.PlayerID(244532);
        Set<Player.PlayerID> players = new HashSet<>();
        Set<Player.PlayerID> players2 = new HashSet<>();
        players.add(p1);
        players.add(p2);
        players2.add(p1);
        players2.add(p2);
        Team t1 = new Team(players);
        Team t2 = new Team(players2);

        assertThat(t1 == t2, is(false));
        assertThat(t1.equals(t2) && t2.equals(t1), is(true));
    }

    @Test
    public void hashCodeIsTheSameForSameTeams() {
        Player.PlayerID p1 = new Player.PlayerID(234832);
        Player.PlayerID p2 = new Player.PlayerID(244532);
        Set<Player.PlayerID> players = new HashSet<>();
        Set<Player.PlayerID> players2 = new HashSet<>();
        players.add(p1);
        players.add(p2);
        players2.add(p1);
        players2.add(p2);
        Team t1 = new Team(players);
        Team t2 = new Team(players2);

        assertThat(t1 == t2, is(false));
        assertThat(t1.equals(t2) && t2.equals(t1), is(true));
        assertThat(t1.hashCode() == t2.hashCode(), is(true));
    }

    @Test
    public void hashCodeIsDifferentForDifferentTeams() {
        Player.PlayerID p1 = new Player.PlayerID(234832);
        Player.PlayerID p2 = new Player.PlayerID(244532);
        Set<Player.PlayerID> players = new HashSet<>();
        players.add(p1);
        Team t1 = new Team(players);
        players.add(p2);
        Team t2 = new Team(players);

        assertThat(t1.hashCode() == t2.hashCode(), is(false));
    }
}