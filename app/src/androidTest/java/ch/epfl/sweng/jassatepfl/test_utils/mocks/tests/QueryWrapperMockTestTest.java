package ch.epfl.sweng.jassatepfl.test_utils.mocks.tests;


import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.LeafTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.RootTest;
import ch.epfl.sweng.jassatepfl.test_utils.mocks.DBRefWrapTest;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class QueryWrapperMockTestTest {

    @Test
    public void queryMockTest() {
        RootTest root = new RootTest("jass@Epfl");
        root.addChild(DatabaseUtils.DATABASE_MATCHES);
        root.addChild(DatabaseUtils.DATABASE_PLAYERS);
        DBRefWrapTest localRef = new DBRefWrapTest(root);

        Set<Player> players = new HashSet<>();
        players.add(DummyDataTest.amaury);
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.privateMatch());
        localRef.addPlayers(players);
        localRef.addMatches(matches);
        ch.epfl.sweng.jassatepfl.test_utils.mocks.QueryWrapperMockTest query = (ch.epfl.sweng.jassatepfl.test_utils.mocks.QueryWrapperMockTest) localRef.child(DatabaseUtils.DATABASE_MATCHES).orderByChild(DatabaseUtils.DATABASE_MATCHES_PRIVATE).equalTo(true);

        try {
            Field f = query.getClass().getDeclaredField("elements");
            f.setAccessible(true);
            List<LeafTest> matchs = (List<LeafTest>) f.get(query);
            assertEquals(1 , matchs.size());
            assertEquals("private", ((Match) matchs.get(0).getData()).getMatchID());
        } catch (Exception e) {
            fail();
        }
    }
}
