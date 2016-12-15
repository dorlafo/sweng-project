package ch.epfl.sweng.jassatepfl.test_utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.test_utils.database.local.MatchLeafTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.PendingMatchLeafTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.NodeTest;
import ch.epfl.sweng.jassatepfl.test_utils.mocks.DBRefWrapTest;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * Utils class for testing DB
 */
public class DBTestUtils {

    public static void assertMatchContainsNPlayers(DBRefWrapTest dbRefWrapTest, String matchID, final int nPlayers) {
        NodeTest db = dbRefWrapTest.getCurrentNode();
        NodeTest matchesDB = db.getChild(DatabaseUtils.DATABASE_MATCHES);
        MatchLeafTest match =(MatchLeafTest) matchesDB.getChild(matchID);
        Match m = match.getData();
        assertEquals(nPlayers, m.getPlayers().size());
    }

    public static void assertMatchContainsPlayer(DBRefWrapTest dbRefWrapTest, String matchID, final Player.PlayerID sciper) {
        NodeTest db = dbRefWrapTest.getCurrentNode();
        NodeTest matchesDB = db.getChild(DatabaseUtils.DATABASE_MATCHES);
        MatchLeafTest match =(MatchLeafTest) matchesDB.getChild(matchID);
        Match m = match.getData();
        boolean isContained = false;
        for(Player p : m.getPlayers()) {
            if(p.getID().equals(sciper)) {
                isContained = true;
            }
        }
        assertTrue(isContained);
    }

    public static void assertMatchesDBContainsNMatches(DBRefWrapTest dbRefWrapTest, final int nMatches) {
        NodeTest db = dbRefWrapTest.getCurrentNode();
        NodeTest matchesDB = db.getChild(DatabaseUtils.DATABASE_MATCHES);
        Set<NodeTest> nodeMatches = matchesDB.getChildren();
        List<Match> matches = new ArrayList<>();
        for(NodeTest matchLeaf: nodeMatches) {
            Match m = ((MatchLeafTest) matchLeaf).getData();
            if(!matchLeaf.isDeleted()) {
                matches.add(m);
            }
        }

        assertEquals(nMatches, matches.size());
    }

    public static void assertPendingMatchContainsNPlayers(DBRefWrapTest dbRefWrapTest, final String matchID, final int nPlayers) {
        NodeTest db = dbRefWrapTest.getCurrentNode();
        NodeTest statusDB = db.getChild(DatabaseUtils.DATABASE_PENDING_MATCHES);
        PendingMatchLeafTest status = (PendingMatchLeafTest) statusDB.getChild(matchID);
        Map<String, Boolean> matchStatus = status.getData();

        assertEquals(nPlayers, matchStatus.size());
    }

}
