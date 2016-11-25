package ch.epfl.sweng.jassatepfl.database.local;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Round;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;

public final class MatchStatsLeaf extends Leaf<MatchStats> {

    private LeafField<String> matchIdLeaf = new LeafField<>("matchID");
    private LeafField<Match.GameVariant> variantLeaf = new LeafField<>("gameVariant");
    private LeafField<Integer> nbTeamLeaf = new LeafField<>("nbTeam");
    private LeafField<List<Round>> roundsLeaf = new LeafField<>("rounds");
    private LeafField<Map<String, Integer>> scoresLeaf = new LeafField<>("totalScores");
    private LeafField<Integer> roundIndexLeaf = new LeafField<>("currentRoundIndex");
    private LeafField<Boolean> goalLeaf = new LeafField<>("goalHasBeenReached");
    private LeafField<Integer> winnerIndexLeaf = new LeafField<>("winnerIndex");
    private LeafField<Boolean> meldSetLeaf = new LeafField<>("meldWasSetThisRound");

    public MatchStatsLeaf(String id) {
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
    public LeafField getChild(String id) {
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
