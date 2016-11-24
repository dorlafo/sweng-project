package ch.epfl.sweng.jassatepfl.stats;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.model.Team;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for MatchStats class
 */
public class MatchStatsTest {
    @Test(expected = IllegalArgumentException.class)
    public void constructorReturnsExceptionWhenTeamSizeIsWrong() {
        Player.PlayerID p1 = new Player.PlayerID("234832");
        Player.PlayerID p2 = new Player.PlayerID("234554");
        List<Player.PlayerID> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        Team t1 = new Team(players);
        List<Team> teams = new ArrayList<>();
        teams.add(t1);

        MatchStats m = new MatchStats("matchID", Match.GameVariant.CHIBRE, teams);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorReturnsExceptionWhenTeamsDoesNotHaveCorrectNumberOfPlayers() {
        Player.PlayerID p11 = new Player.PlayerID("234832");
        Player.PlayerID p12 = new Player.PlayerID("234554");

        Player.PlayerID p21 = new Player.PlayerID("123456");


        List<Player.PlayerID> players1 = new ArrayList<>();
        List<Player.PlayerID> players2 = new ArrayList<>();
        players1.add(p11);
        players1.add(p12);

        players2.add(p21);

        Team t1 = new Team(players1);
        Team t2 = new Team(players2);
        List<Team> teams = new ArrayList<>();
        teams.add(t1);
        teams.add(t2);

        MatchStats m = new MatchStats("matchID", Match.GameVariant.CHIBRE, teams);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorReturnsExceptionWhenSomeTeamsAreIdentical() {
        Player.PlayerID p11 = new Player.PlayerID("234832");
        Player.PlayerID p12 = new Player.PlayerID("234554");

        List<Player.PlayerID> players1 = new ArrayList<>();
        List<Player.PlayerID> players2 = new ArrayList<>();

        players1.add(p11);
        players1.add(p12);
        players2.add(p11);
        players2.add(p12);

        Team t1 = new Team(players1);
        Team t2 = new Team(players2);
        List<Team> teams = new ArrayList<>();
        teams.add(t1);
        teams.add(t2);

        MatchStats m = new MatchStats("matchID", Match.GameVariant.CHIBRE, teams);
    }
}