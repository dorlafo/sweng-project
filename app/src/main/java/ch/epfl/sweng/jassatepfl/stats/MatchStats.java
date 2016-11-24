package ch.epfl.sweng.jassatepfl.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match.GameVariant;
import ch.epfl.sweng.jassatepfl.model.Match.Meld;
import ch.epfl.sweng.jassatepfl.model.Round;

/**
 * This class contains the methods and fields necessary to make statistics about a match and to
 * be used to count points during the match
 */

public class MatchStats {

    // The unique identifiers of the match used in the database
    private String matchID;
    // The match' gameVariant. Used to choose how points are counted and so on
    private GameVariant gameVariant;
    private int nbTeam;
    // Array containing the score for each round by team
    private ArrayList<Round> rounds;
    private Map<String, Integer> totalScores;
    // Index to the current round
    private int currentRoundIndex;
    private boolean goalHasBeenReached;
    private int winnerIndex;

    public MatchStats() {
    }

    public MatchStats(String matchID, GameVariant gameVariant) throws IllegalArgumentException {
        this.matchID = matchID;
        this.gameVariant = gameVariant;
        this.nbTeam = gameVariant.getNumberOfTeam();
        this.rounds = new ArrayList<>();
        this.rounds.add(new Round(nbTeam));
        this.totalScores = new HashMap<>();
        for (int i = 0; i < nbTeam; ++i) {
            totalScores.put(concatKey(i), 0);
        }
        this.currentRoundIndex = 0;
        this.goalHasBeenReached = false;
        this.winnerIndex = -1;
    }

    /**
     * Getter for the matchID
     *
     * @return The matchID
     */
    public String getMatchID() {
        return matchID;
    }

    /**
     * Getter for the game variant of this match
     *
     * @return The game variant
     */
    public GameVariant getGameVariant() {
        return gameVariant;
    }

    public int getNbTeam() {
        return nbTeam;
    }

    public int getCurrentRoundIndex() {
        return currentRoundIndex;
    }

    public boolean goalHasBeenReached() {
        return goalHasBeenReached;
    }

    public int getWinnerIndex() {
        return winnerIndex;
    }

    public Integer getCurrentRoundTeamScore(int teamIndex) {
        if (teamIndex < 0 || teamIndex >= nbTeam) {
            throw new IllegalArgumentException("Invalid team index");
        }
        return rounds.get(currentRoundIndex).getTotalRoundScore(teamIndex);
    }

    public Integer getTotalMatchScore(int teamIndex) {
        if (teamIndex < 0 || teamIndex >= nbTeam) {
            throw new IllegalArgumentException("Invalid team index");
        }
        return totalScores.get(concatKey(teamIndex));
    }

    /**
     * Adds a new round to the match.
     */
    public void finishRound() {
        for (int i = 0; i < nbTeam; ++i) {
            String key = concatKey(i);
            Integer tmp = totalScores.get(key);
            tmp += rounds.get(currentRoundIndex).getTotalRoundScore(i);
            totalScores.put(key, tmp);
            goalHasBeenReached |= tmp >= gameVariant.getPointGoal();
            winnerIndex = goalHasBeenReached && winnerIndex == -1 ? i : winnerIndex;
        }
        Round round = new Round(nbTeam);
        rounds.add(round);
        ++currentRoundIndex;
    }

    public void cancelLastRound() {
        if (currentRoundIndex == 0) {
            throw new IllegalStateException();
        }
        --currentRoundIndex;
        for (int i = 0; i < nbTeam; ++i) {
            String key = concatKey(i);
            Integer tmp = totalScores.get(key);
            tmp -= rounds.get(currentRoundIndex).getTotalRoundScore(i);
            totalScores.put(key, tmp);
        }
        rounds.remove(currentRoundIndex);
    }

    public void setScore(int teamIndex, int score) {
        if (teamIndex < 0 || teamIndex >= nbTeam) {
            throw new IndexOutOfBoundsException();
        }
        rounds.get(currentRoundIndex).setScore(teamIndex, score);
    }

    /**
     * Sets the meld to the corresponding team.
     *
     * @param teamIndex the index of the team
     * @param meld      the meld
     */
    public void setMeld(int teamIndex, Meld meld) {
        if (teamIndex < 0 || teamIndex >= nbTeam) {
            throw new IndexOutOfBoundsException();
        }
        rounds.get(currentRoundIndex).addMeld(teamIndex, meld);
    }

    private String concatKey(int index) {
        return "Team" + index;
    }

}
