package ch.epfl.sweng.jassatepfl.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a team. It contains the ID's of all team members
 */

public class Team {
    //Array containing the ID of the team members
    private final List<Player.PlayerID> members;

    public Team(List<Player.PlayerID> memberIDs) {
        ArrayList<Player.PlayerID> tmpMembers = new ArrayList<>();
        for(Player.PlayerID member : memberIDs) {
            tmpMembers.add(member);
        }
        this.members = Collections.unmodifiableList(tmpMembers);
    }

    /**
     * Getter for the number of players in the team
     * @return Returns the number of players in the team
     */
    public int getNumberOfMembers() {
        return members.size();
    }

    /**
     * Getter for the players
     * @return Returns a list of the members of the team
     */
    public List<Player.PlayerID> getMembers() {
        return Collections.unmodifiableList(members);
    }
}
