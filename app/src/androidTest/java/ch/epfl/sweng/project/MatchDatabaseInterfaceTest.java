package ch.epfl.sweng.project;

import org.junit.Test;

import java.security.ProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.project.database.MatchDatabaseInterface;
import ch.epfl.sweng.project.model.GPSPoint;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.model.Rank;

import static junit.framework.Assert.fail;


/**
 * Test class for the MatchDatabaseInterface
 */
public final class MatchDatabaseInterfaceTest {
    private static Player player1 = new Player(new Player.PlayerID(1234), "Keeppo", "Kappa", new Rank(1000));
    private static Player player2 = new Player(new Player.PlayerID(4321), "Lisa", "Bob", new Rank(1500));
    private static Player player3 = new Player(new Player.PlayerID(1111), "Stocrate", "Harry", new Rank(753));
    private static Player player4 = new Player(new Player.PlayerID(1111), "Gole", "Jerry", new Rank(2153));
    private static List<Player> playerList1 = new ArrayList<>(Arrays.asList(player1, player2));
    private static GPSPoint location1 = new GPSPoint(40.3, 23.2);
    private static Match match1 = new Match(playerList1, location1, "tout au bout de mes rêves", false, 614430);

    @Test
    public void writeNewMatchCompleted() {
        MatchDatabaseInterface mProvider = new MatchDatabaseInterface();
        try {
            mProvider.writeNewMatch(match1);
        } catch (ProviderException e) {
            fail("Unable to get comfirmation from the server");
        }
    }

    @Test
    public void deleteMatchCompleted() {
        MatchDatabaseInterface mProvider = new MatchDatabaseInterface();
        try {
            mProvider.deleteMatch("test");
        } catch (ProviderException e) {
            fail("Unable to get comfirmation from the server");
        }
    }

    @Test
    public void updateMatchCompleted() {
        MatchDatabaseInterface mProvider = new MatchDatabaseInterface();
        try {
            mProvider.updateMatch("test", match1);
        } catch (ProviderException e) {
            fail("Unable to get comfirmation from the server");
        }
    }

    //TODO in Sprint #4: Test the MatchDatabaseInterface using mocks (mockito)
}
