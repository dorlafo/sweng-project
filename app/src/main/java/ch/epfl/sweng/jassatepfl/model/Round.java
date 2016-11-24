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

    private final static String TEAM = "Team ";

    private int teamCount;
    private Map<String, Integer> scores;
    private Map<String, List<Meld>> melds;

    public Round() {
    }

    public Round(int teamCount) {
        this.teamCount = teamCount;
        this.scores = new HashMap<>();
        this.melds = new HashMap<>();
        for (int i = 0; i < teamCount; ++i) {
            scores.put(TEAM + i, 0);
            melds.put(TEAM + i, new ArrayList<Meld>());
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
    public Collection<Integer> getScores() {
        return scores.values();
    }

    /**
     * Returns the score of the specified team.
     *
     * @param teamIndex the index of the team
     * @return the score of the team for this round
     */
    public Integer getTeamScore(int teamIndex) {
        if (teamIndex < 0 || teamIndex >= teamCount) {
            throw new IndexOutOfBoundsException();
        }
        return scores.get(TEAM + teamIndex);
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
        return Collections.unmodifiableList(melds.get(TEAM + teamIndex));
    }

    /**
     * Adds the points to the round.
     *
     * @param scores the points made by the teams
     */
    public void setScores(int... scores) {
        if (scores.length != teamCount) {
            throw new IllegalArgumentException("Number of points does not match number of teams.");
        }
        for (int i = 0; i < teamCount; ++i) {
            this.scores.put(TEAM + i, scores[i]);
        }
    }

    public void addToTeamScore(int teamIndex, int points) {
        if (teamIndex < 0 || teamIndex >= teamCount) {
            throw new IndexOutOfBoundsException();
        }
        Integer currentPoints = scores.get(TEAM + teamIndex);
        scores.put(TEAM + teamIndex, currentPoints + points);
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
        melds.get(TEAM + teamIndex).add(meld);
        addToTeamScore(teamIndex, meld.value());
    }

}
