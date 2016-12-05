package ch.epfl.sweng.jassatepfl.stats;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.dorian;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.marco;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.random;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.vincenzo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UserStatsTest {

    @Test
    public void getPlayerId() throws Exception {
        UserStats stats = stats();
        assertThat(stats.getPlayerId().equals(random.getID()), is(true));
        assertThat(stats.getPlayerId().equals(new Player.PlayerID("010000")), is(false));
    }

    @Test
    public void getPlayedMatches() throws Exception {
        UserStats stats = stats();
        assertThat(stats.getPlayedMatches(), is(1));
    }

    @Test
    public void getWonMatches() throws Exception {
        UserStats stats = stats();
        assertThat(stats.getWonMatches(), is(1));
    }

    @Test
    public void getPlayedByDate() throws Exception {
        UserStats stats = stats();
        List<Tuple2<Long, Integer>> ls = stats.getPlayedByDate();
        assertThat(ls.size(), is(1));
        assertThat(ls.get(0).getValue(), is(1));
    }

    @Test
    public void getWonByDate() throws Exception {
        UserStats stats = stats();
        List<Tuple2<Long, Integer>> ls = stats.getWonByDate();
        assertThat(ls.size(), is(1));
        assertThat(ls.get(0).getValue(), is(1));
    }

    @Test
    public void getRankByDate() throws Exception {
        UserStats stats = stats();
        List<Tuple2<Long, Integer>> ls = stats.getQuoteByDate();
        assertThat(ls.size(), is(1));
        int correctQuote = 0;
        assertThat(correctQuote == ls.get(0).getValue(), is(true));
    }

    @Test
    public void getVariants() throws Exception {
        UserStats stats = stats();
        Map<String, Integer> mp = stats.getVariants();
        assertThat(mp.size(), is(1));
        assertThat(mp.get(Match.GameVariant.CHIBRE.toString()), is(1));
    }

    @Test
    public void getPartners() throws Exception {
        UserStats stats = stats();
        Map<String, Integer> mp = stats.getPartners();
        assertThat(mp.size(), is(1));
        assertThat(mp.get("666666"), is(1));
        assertThat(mp.get("111111") == null, is(true));
    }

    @Test
    public void getWonWith() throws Exception {
        UserStats stats = stats();
        Map<String, Integer> mp = stats.getWonWith();
        assertThat(mp.size(), is(1));
        assertThat(mp.get("666666"), is(1));
        assertThat(mp.get("111111") == null, is(true));
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void updateRank() throws Exception {
        UserStats stats = stats();
        stats.updateRank(new NaiveCalculator(stats));
        assertThat(stats.getQuoteByDate().size(), is(1));
        assertThat(1 == stats.getQuoteByDate().get(0).getValue(), is(true));
    }

    private UserStats stats() {
        Match match = DummyDataTest.fullMatch();
        match.setTeam(0, random.getID());
        match.setTeam(0, marco.getID());
        match.setTeam(1, dorian.getID());
        match.setTeam(1, vincenzo.getID());
        MatchStats matchStats = new MatchStats(match);
        matchStats.setScore(0, 1000);
        matchStats.setScore(1, 500);
        matchStats.setWinnerIndex(0);
        UserStats stats = new UserStats(random.getID());
        stats.update(matchStats);
        return stats;
    }

}
