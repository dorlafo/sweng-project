package ch.epfl.sweng.project;

import org.junit.Test;
import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.project.database.MatchProvider;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.model.Rank;
import ch.epfl.sweng.project.model.GPSPoint;

public final class ProviderTest {
    private static Player player1 = new Player(new Player.PlayerID(1234), "Keeppo", "Kappa", new Rank(1000));
    private static Player player2 = new Player(new Player.PlayerID(4321), "Lisa", "Bob", new Rank(1500));
    private static Player player3 = new Player(new Player.PlayerID(1111), "Stocrate", "Harry", new Rank(753));
    private static Player player4 = new Player(new Player.PlayerID(1111), "Gole", "Jerry", new Rank(2153));
    private static List<Player> playerList1 = new ArrayList<Player>(Arrays.asList(player1, player2));
    private static GPSPoint location1 = new GPSPoint(40.3, 23.2);
    private static Match match1 = new Match(playerList1, location1, "tout au bout de mes rêves", false, 614430);


    @Test
    public void deleteMatchFromDBTest() {
        MatchProvider mProvider = new MatchProvider();
        String token = mProvider.writeNewMatchToDB(match1);
        mProvider.deleteMatchFromDB(token);
        assertFalse(mProvider.provide().containsKey(token));
    }

    @Test
    public void writeNewMatchToDB() {
        MatchProvider mProvider = new MatchProvider();
        String token = mProvider.writeNewMatchToDB(match1);
        assertTrue(mProvider.provide().containsKey(token));
        mProvider.deleteMatchFromDB(token);
    }
}
