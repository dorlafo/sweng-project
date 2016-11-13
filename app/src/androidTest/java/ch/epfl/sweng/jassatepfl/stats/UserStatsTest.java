package ch.epfl.sweng.jassatepfl.stats;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.model.Rank;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by vinz on 11/12/16.
 */
public class UserStatsTest {

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

    @Test
    public void getPlayerId() throws Exception {
        assertThat(stats.getPlayerId().equals(new Player.PlayerID("000000")), is(true));
        assertThat(stats.getPlayerId().equals(new Player.PlayerID("010000")), is(false));
    }

    @Test
    public void getPlayedMatches() throws Exception {
        assertThat(stats.getPlayedMatches(), is(1));
    }

    @Test
    public void getWonMatches() throws Exception {
        assertThat(stats.getWonMatches(), is(1));
    }

    @Test
    public void getPlayedByDate() throws Exception {
        List<Tuple2<Long, Integer>> ls = stats.getPlayedByDate();
        assertThat(ls.size(), is(1));
        assertThat(ls.get(0).getValue(), is(1));
    }

    @Test
    public void getWonByDate() throws Exception {
        List<Tuple2<Long, Integer>> ls = stats.getWonByDate();
        assertThat(ls.size(), is(1));
        assertThat(ls.get(0).getValue(), is(1));
    }

    @Test
    public void getRankByDate() throws Exception {
        List<Tuple2<Long, Rank>> ls = stats.getRankByDate();
        assertThat(ls.size(), is(1));
        Rank correctRank = new Rank(0);
        assertThat(correctRank.equals(ls.get(0).getValue()), is(true));
    }

    @Test
    public void getVariants() throws Exception {
        Map<String, Integer> mp = stats.getVariants();
        assertThat(mp.size(), is(1));
        assertThat(mp.get(Match.GameVariant.CLASSIC.toString()), is(1));
    }

    @Test
    public void getPartners() throws Exception {
        Map<String, Integer> mp = stats.getPartners();
        assertThat(mp.size(), is(1));
        assertThat(mp.get("333333"), is(1));
        assertThat(mp.get("111111")== null, is(true));
    }

    @Test
    public void getWonWith() throws Exception {
        Map<String, Integer> mp = stats.getWonWith();
        assertThat(mp.size(), is(1));
        assertThat(mp.get("333333"), is(1));
        assertThat(mp.get("111111") == null, is(true));
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void updateRank() throws Exception {
        stats.updateRank(new NaiveCalculator(stats));
        assertThat(stats.getRankByDate().size(), is(1));
        assertThat(new Rank(1).equals(stats.getRankByDate().get(0).getValue()) , is(true));
    }

}