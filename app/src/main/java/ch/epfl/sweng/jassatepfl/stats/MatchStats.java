package ch.epfl.sweng.jassatepfl.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Round;
import ch.epfl.sweng.jassatepfl.model.Team;

/**
 * This class contains the methods and fields necessary to make statistics about a match and to
 * be used to count points during the match
 */

public class MatchStats {

    //The unique identifiers of the match used in the database
    private String matchID;
    //The match' gameVariant. Used to choose how points are counted and so on
    private Match.GameVariant gameVariant;
    //Array containing the team of this match
    private List<Team> teams;
    private int nbTeam;
    //Array containing the score for each round by team
    private ArrayList<ArrayList<Round>> scores;
    //Index to the current round
    private int currentRoundIndex;

    public MatchStats() {

    }

    public MatchStats(String matchID, Match.GameVariant gameVariant, List<Team> teams) throws IllegalArgumentException {
        this.nbTeam = gameVariant.getNumberOfTeam();
        if(teams.size() != this.nbTeam) {
            throw new IllegalArgumentException("Invalid number of teams");
        }

        for(Team t : teams) {
            if (t.getNumberOfMembers() != gameVariant.getNumberOfPlayerByTeam()) {
                throw new IllegalArgumentException("Invalid number of member in some team");
            }
        }

        ArrayList<Team> tmpTeams = new ArrayList<>();
        for(Team t : teams) {
            if(tmpTeams.contains(t)) {
                throw new IllegalArgumentException("Invalid list of team. A match cannot have identical teams");
            }
            else {
                tmpTeams.add(t);
            }
        }

        this.matchID = matchID;
        this.gameVariant = gameVariant;
        this.teams = Collections.unmodifiableList(tmpTeams);

        this.scores = new ArrayList<>(nbTeam);
        for(ArrayList<Round> r : scores) {
            r = new ArrayList<>();
        }
        this.currentRoundIndex = 0;
    }

    /**
     * Set the score for the current round. The array contains the number of point for each team in
     * order
     * @param nbPoints the number of points made by each team in the current round
     * @throws IllegalArgumentException If the number of score is not valid (ie not equal to the number of teams)
    */
    public void setScore(int[] nbPoints) throws IllegalArgumentException {
        if(nbPoints.length != nbTeam) {
            throw new IllegalArgumentException("Invalid number of scores");
        }
        for(int i = 0; i < nbTeam; ++i) {
            scores.get(i).get(currentRoundIndex).addPoints(nbPoints[i]);
        }
        currentRoundIndex++;
    }

    /**
     * Set the meld to the corresponding team
     * @param m The meld made
     * @param teamIndex The team that made the bid
     */
    public void setMeld(Match.Meld m, int teamIndex) {
        scores.get(teamIndex).get(currentRoundIndex).addMeld(m);
    }

    /**
     * Getter for the matchID
     * @return The matchID
     */
    public String getMatchID() {
        return this.matchID;
    }

    /**
     * Getter for the game variant of this match
     * @return The game variant
     */
    public Match.GameVariant getGameVariant() {
        return this.gameVariant;
    }

    /**
     * Getter for the teams of this match
     * @return A list of the teams
     */
    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    /**
     * Getter for the specified team
     * @param teamIndex The index of the team we want
     * @return The team wanted
     * @throws IllegalArgumentException If the teamIndex is not valid
     */
    public Team getTeam(int teamIndex) throws IllegalArgumentException {
        if(teamIndex < 0 || teamIndex >= nbTeam) {
            throw new IllegalArgumentException("Invalid team index");
        }
        return teams.get(teamIndex);
    }

    public List<Round> getScore(int teamIndex) throws IllegalArgumentException {
        if(teamIndex < 0 || teamIndex >= nbTeam) {
            throw new IllegalArgumentException("Invalid team index");
        }
        return Collections.unmodifiableList(scores.get(teamIndex));
    }

    public int getNbTeam() {
        return nbTeam;
    }

    public int getCurrentRoundIndex() {
        return currentRoundIndex;
    }
}

