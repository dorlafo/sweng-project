package ch.epfl.sweng.jassatepfl.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class representing a team. It contains the ID's of all team members
 */

public class Team {
    //Set containing the ID of the team members
    private final Set<Player.PlayerID> members;

    /**
     * Public constructor for Team
     * @param memberIDs A set containing the ID's of the player
     * @throws IllegalArgumentException If the set of ID's is empty
     */
    public Team(Set<Player.PlayerID> memberIDs) throws IllegalArgumentException {
        if(memberIDs.size() == 0) {
            throw new IllegalArgumentException("The team cannot have zero member");
        }
        HashSet<Player.PlayerID> tmpMembers = new HashSet<>();
        for(Player.PlayerID member : memberIDs) {
            tmpMembers.add(member);
        }
        this.members = Collections.unmodifiableSet(tmpMembers);
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
    public Set<Player.PlayerID> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        else if (o == this) {
            return true;
        }
        else if (o.getClass().getName() != this.getClass().getName()) {
            return false;
        }
        else {
            if (((Team)o).members.equals(this.members)) {
                return true;
            }
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(members);
    }
}
