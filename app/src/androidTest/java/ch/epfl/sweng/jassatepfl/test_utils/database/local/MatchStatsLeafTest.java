package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Round;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;

public final class MatchStatsLeafTest extends LeafTest<MatchStats> {

    private LeafFieldTest<String> matchIdLeaf = new LeafFieldTest<>("matchID");
    private LeafFieldTest<Match.GameVariant> variantLeaf = new LeafFieldTest<>("gameVariant");
    private LeafFieldTest<Integer> nbTeamLeaf = new LeafFieldTest<>("nbTeam");
    private LeafFieldTest<List<Round>> roundsLeaf = new LeafFieldTest<>("rounds");
    private LeafFieldTest<Map<String, Integer>> scoresLeaf = new LeafFieldTest<>("totalScores");
    private LeafFieldTest<Integer> roundIndexLeaf = new LeafFieldTest<>("currentRoundIndex");
    private LeafFieldTest<Boolean> goalLeaf = new LeafFieldTest<>("goalHasBeenReached");
    private LeafFieldTest<Integer> winnerIndexLeaf = new LeafFieldTest<>("winnerIndex");
    private LeafFieldTest<Boolean> meldSetLeaf = new LeafFieldTest<>("meldWasSetThisRound");

    public MatchStatsLeafTest(String id) {
        super(id);
    }

    @Override
    public void setData(MatchStats data) {
        this.data = data;
        matchIdLeaf.setData(data.getMatchID());
        variantLeaf.setData(data.getGameVariant());
        nbTeamLeaf.setData(data.getNbTeam());
        roundsLeaf.setData(data.getRounds());
        scoresLeaf.setData(data.getTotalScores());
        roundIndexLeaf.setData(data.getCurrentRoundIndex());
        goalLeaf.setData(data.goalHasBeenReached());
        winnerIndexLeaf.setData(data.getWinnerIndex());
        meldSetLeaf.setData(data.meldWasSetThisRound());
    }

    @Override
    public LeafFieldTest getChild(String id) {
        switch (id) {
            case "matchID":
                return matchIdLeaf;
            case "gameVariant":
                return variantLeaf;
            case "nbTeam":
                return nbTeamLeaf;
            case "rounds":
                return roundsLeaf;
            case "totalScores":
                return scoresLeaf;
            case "currentRoundIndex":
                return roundIndexLeaf;
            case "goalHasBeenReached":
                return goalLeaf;
            case "winnerIndex":
                return winnerIndexLeaf;
            case "meldWasSetThisRound":
                return meldSetLeaf;
            default:
                throw new IllegalArgumentException("MatchStats class does not have a field named : " + id);
        }
    }

}
