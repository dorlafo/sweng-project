package ch.epfl.sweng.jassatepfl.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match.Meld;

/**
 * Class representing a round object. It contains the points and melds obtained in the round.
 */
public class Round {

    private int teamCount;
    private Map<String, Integer> scores;
    private Map<String, List<Meld>> melds;
    private Map<String, Integer> meldScores;

    public Round() {
    }

    public Round(int teamCount) {
        this.teamCount = teamCount;
        this.scores = new HashMap<>();
        this.melds = new HashMap<>();
        this.meldScores = new HashMap<>();

        for (int i = 0; i < teamCount; ++i) {
            String key = concatKey(i);
            scores.put(key, 0);
            melds.put(key, new ArrayList<Meld>());
            meldScores.put(key, 0);
        }
    }

    public int getTeamCount() {
        return teamCount;
    }

    /**
     * Getter for the points in this round
     *
     * @return The points made
     */
    public Collection<Integer> getScores() { // TODO: maybe delete this
        return scores.values();
    }

    /**
     * Returns the score of the specified team.
     *
     * @param teamIndex the index of the team
     * @return the score of the team for this round
     */
    public Integer getRoundTeamScore(int teamIndex) {
        if (teamIndex < 0 || teamIndex >= teamCount) {
            throw new IndexOutOfBoundsException();
        }
        return scores.get(concatKey(teamIndex));
    }

    public Integer getRoundMeldScore(int teamIndex) {
        if (teamIndex < 0 || teamIndex >= teamCount) {
            throw new IndexOutOfBoundsException();
        }
        return meldScores.get(concatKey(teamIndex));
    }

    public Integer getTotalRoundScore(int teamIndex) {
        if (teamIndex < 0 || teamIndex >= teamCount) {
            throw new IndexOutOfBoundsException();
        }
        return getRoundTeamScore(teamIndex) + getRoundMeldScore(teamIndex);
    }

    /**
     * Returns the melds obtained by the specified team in this round.
     *
     * @param teamIndex the index of the team
     * @return the melds
     */
    public List<Meld> getMelds(int teamIndex) {
        if (teamIndex < 0 || teamIndex >= teamCount) {
            throw new IndexOutOfBoundsException();
        }
        return Collections.unmodifiableList(melds.get(concatKey(teamIndex)));
    }

    public void setScore(int teamIndex, int score) {
        if (teamIndex < 0 || teamIndex >= teamCount) {
            throw new IndexOutOfBoundsException();
        }
        scores.put(concatKey(teamIndex), score);
    }

    /**
     * Adds the meld passed in parameters to the current meld list and the points
     * for this meld to the points.
     *
     * @param teamIndex the index of the team that got the meld
     * @param meld      the meld
     */
    public void addMeld(int teamIndex, Meld meld) {
        if (teamIndex < 0 || teamIndex >= teamCount) {
            throw new IndexOutOfBoundsException();
        }

        String key = concatKey(teamIndex);
        melds.get(key).add(meld);
        Integer tmp = meldScores.get(key);
        meldScores.put(key, tmp + meld.value());
    }

    private String concatKey(int index) {
        return "Team" + index;
    }

}
