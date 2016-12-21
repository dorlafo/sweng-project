package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Round;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;

public final class MatchStatsLeafTest extends LeafTest<MatchStats> {

    private LeafFieldTest<String> matchIdLeaf = new LeafFieldTest<>("matchID", this);
    private LeafFieldTest<Match.GameVariant> variantLeaf = new LeafFieldTest<>("gameVariant", this);
    private LeafFieldTest<Integer> nbTeamLeaf = new LeafFieldTest<>("nbTeam", this);
    private LeafFieldTest<List<Round>> roundsLeaf = new LeafFieldTest<>("rounds", this);
    private LeafFieldTest<Map<String, Integer>> scoresLeaf = new LeafFieldTest<>("totalScores", this);
    private LeafFieldTest<Integer> roundIndexLeaf = new LeafFieldTest<>("currentRoundIndex", this);
    private LeafFieldTest<Boolean> goalLeaf = new LeafFieldTest<>("goalHasBeenReached", this);
    private LeafFieldTest<Integer> winnerIndexLeaf = new LeafFieldTest<>("winnerIndex", this);
    private LeafFieldTest<Boolean> meldSetLeaf = new LeafFieldTest<>("meldWasSetThisRound", this);

    public MatchStatsLeafTest(String id, NodeTest parent) {
        super(id, parent);
    }

    @Override
    public void setData(MatchStats data) {
        if(isDeleted) {
            //throw new UnsupportedOperationException("Cannot setData on a deleted MatchStatsLeafTest");
            this.isDeleted = false;
        }
        this.data = data;
        matchIdLeaf.setData(data.getMatchID());
        variantLeaf.setData(data.obtainGameVariant());
        nbTeamLeaf.setData(data.getNbTeam());
        roundsLeaf.setData(data.getRounds());
        scoresLeaf.setData(data.getTotalScores());
        roundIndexLeaf.setData(data.getCurrentRoundIndex());
        goalLeaf.setData(data.goalHasBeenReached());
        winnerIndexLeaf.setData(data.getWinnerIndex());
        meldSetLeaf.setData(data.meldWasSetThisRound());
        this.setChanged();
        this.notifyObservers(this);
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
