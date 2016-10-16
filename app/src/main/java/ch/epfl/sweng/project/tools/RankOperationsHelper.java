package ch.epfl.sweng.project.tools;

import java.util.List;

import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.model.Rank;

public final class RankOperationsHelper {

    public static Rank averageRank(List<Player> players) {
        Rank total = new Rank(0);
        int numPlayers = 0;

        for (Player player : players) {
            total = total.add(player.getRank());
            ++numPlayers;
        }

        return numPlayers == 0 ? total : new Rank(total.getRank() / numPlayers); // TODO: use ceiling function?
    }

}
