package ch.epfl.sweng.jassatepfl.stats.trueskill;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static ch.epfl.sweng.jassatepfl.stats.trueskill.SkillCalculator.calculateNewRatings;
import static junit.framework.Assert.assertEquals;


/**
 * @author Amaury Combes
 */
public class SkillCalculatorTest {

    @Test
    public void userShouldGetABetterRankWhenWinningHardMatchTest() {
        Rank userRank = new Rank(10, 3);
        Rank player2 = new Rank(10, 3);
        Rank player3 = new Rank(20, 2);
        Rank player4 = new Rank(20, 2);

        List<Rank> players = Arrays.asList(userRank, player2, player3, player4);

        Rank newUserRank = calculateNewRatings(GameInfo.getDefaultGameInfo(), players, SkillCalculator.OutCome.USERTEAM);
        assertEquals(userRank.getRank()  < newUserRank.getRank(), true);
    }

    @Test
    public void userShouldGetABetterRankWhenWinningEasyMatchTest() {
        Rank userRank = new Rank(20, 2);
        Rank player2 = new Rank(20, 2);
        Rank player3 = new Rank(10, 3);
        Rank player4 = new Rank(10, 3);

        List<Rank> players = Arrays.asList(userRank, player2, player3, player4);

        Rank newUserRank = calculateNewRatings(GameInfo.getDefaultGameInfo(), players, SkillCalculator.OutCome.USERTEAM);
        assertEquals(userRank.getRank() < newUserRank.getRank(), true);
    }

    @Test
    public void userShouldGetALowerRankWhenLoosingHardMatchTest() {
        Rank userRank = new Rank(9, 3);
        Rank player2 = new Rank(9, 3);
        Rank player3 = new Rank(20, 2);
        Rank player4 = new Rank(20, 2);

        List<Rank> players = Arrays.asList(userRank, player2, player3, player4);

        Rank newUserRank = calculateNewRatings(GameInfo.getDefaultGameInfo(), players, SkillCalculator.OutCome.OTHERTEAM);

        assertEquals(userRank.getRank() < newUserRank.getRank(), false);
    }

    @Test
    public void userShouldGetALowerRankWhenLoosingEasyMatchTest() {
        Rank userRank = new Rank(20, 2);
        Rank player2 = new Rank(20, 2);
        Rank player3 = new Rank(10, 3);
        Rank player4 = new Rank(10, 3);

        List<Rank> players = Arrays.asList(userRank, player2, player3, player4);

        Rank newUserRank = calculateNewRatings(GameInfo.getDefaultGameInfo(), players, SkillCalculator.OutCome.OTHERTEAM);
        assertEquals(userRank.getRank() < newUserRank.getRank(), false);
    }

    @Test
    public void userShouldGetAHigherRankWhenDrawTest() {
        Rank userRank = new Rank(20, 2);
        Rank player2 = new Rank(20, 2);
        Rank player3 = new Rank(10, 3);
        Rank player4 = new Rank(10, 3);

        List<Rank> players = Arrays.asList(userRank, player2, player3, player4);

        Rank newUserRank = calculateNewRatings(GameInfo.getDefaultGameInfo(), players, SkillCalculator.OutCome.DRAW);
        assertEquals(userRank.getRank() < newUserRank.getRank(), true);
    }
}
