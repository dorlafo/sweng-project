package ch.epfl.sweng.jassatepfl.model;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
/**
 * Test for the Team class
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
}