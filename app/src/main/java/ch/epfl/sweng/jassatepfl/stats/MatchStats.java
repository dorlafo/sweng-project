package ch.epfl.sweng.jassatepfl.stats;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Team;

/**
 * This class contains the methods and fields necessary to make statistics about a match and to
 * be used to count points during the match
 */

public class MatchStats {

    //The unique identifiers of the match used in the database
    private final String matchID;
    //The match' gameVariant. Used to choose how points are counted and so on
    private final Match.GameVariant gameVariant;
    //Array containing the player's sciper of each team
    private final Team[] teams;
    private final int nbTeam;
    //Array containing the team's score for each round
    private final ArrayList<Integer>[] teamScores;
    //Index to the current round
    private int roundIndex;

    public MatchStats(String matchID, Match.GameVariant gameVariant, List<Team> teams) {
        this.nbTeam = gameVariant.getNumberOfTeam();
        if(teams.size() != this.nbTeam) {
            throw new IllegalArgumentException("Invalid number of teams");
        }

        for(Team t : teams) {
            if (t.getNumberOfMembers() != gameVariant.getNumberOfPlayerByTeam()) {
                throw new IllegalArgumentException("Invalid number of member in some team");
            }
        }

        this.matchID = matchID;
        this.gameVariant = gameVariant;
        this.teams = new Team[nbTeam];

        this.teamScores = new ArrayList[gameVariant.getNumberOfTeam()];
        for(ArrayList<Integer> teamScore : teamScores) {
            teamScore = new ArrayList<>();
        }

        this.roundIndex = 0;
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
            teamScores[i].add(roundIndex, teamScores[i].get(roundIndex) + nbPoints[i]);
        }
        roundIndex++;
    }

    /**
     * Set the meld to the corresponding team
     * @param m The meld made
     * @param teamIndex The team that made the bid
     */
    public void setMeld(Match.Meld m, int teamIndex) {
        teamScores[teamIndex].add(roundIndex, teamScores[teamIndex].get(roundIndex) + m.value());
    }
}
