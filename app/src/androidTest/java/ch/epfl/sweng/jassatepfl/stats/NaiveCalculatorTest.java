package ch.epfl.sweng.jassatepfl.stats;

import org.junit.Test;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.model.Rank;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by vinz on 11/12/16.
 */
public class NaiveCalculatorTest {
    Player.PlayerID id = new Player.PlayerID("000000");
    StatsUpdate update = new StatsUpdate.Builder()
            .addLosers(new Player.PlayerID("111111"), new Player.PlayerID("222222"))
            .addWinners(new Player.PlayerID("333333"), new Player.PlayerID("000000"))
            .setTimestamp(1478908800)
            .setWinScore(700)
            .setLoseScore(200)
            .setGameVariant(Match.GameVariant.CLASSIC)
            .setMatchId("lol")
            .build();
    UserStats stats = new UserStats(id).update(update);
    NaiveCalculator nv = new NaiveCalculator(stats);

    @Test
    public void computeNewRank() throws Exception {
        assertThat(nv.computeNewRank().equals(new Rank(1)), is(true));
    }

}