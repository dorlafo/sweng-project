package ch.epfl.sweng.jassatepfl.mocks;


import android.widget.TextView;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.database.local.Root;
import ch.epfl.sweng.jassatepfl.database.local.reference.DBRefWrapMock;
import ch.epfl.sweng.jassatepfl.database.local.reference.QueryWrapperMock;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.model.Rank;
import ch.epfl.sweng.jassatepfl.test_utils.DummyData;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class QueryWrapperMockTest {

    @Test
    public void queryMockTest() {
        Root root = new Root("jass@Epfl");
        root.addChild("matches");
        root.addChild("players");
        DBRefWrapMock localRef = new DBRefWrapMock(root);

        Set<Player> players = new HashSet<>();
        players.add(DummyData.amaury);
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.privateMatch());
        localRef.addPlayers(players);
        localRef.addMatches(matches);
        QueryWrapperMock query = (QueryWrapperMock) localRef.child("matches").orderByChild("privateMatch").equalTo(true);

        try {
            Field f = query.getClass().getDeclaredField("elements");
            f.setAccessible(true);
            List<Object> matchs = (List<Object>) f.get(query);
            assertEquals(1 , matchs.size());
            assertEquals("private", ((Match) matchs.get(0)).getMatchID());
        } catch (Exception e) {
            fail();
        }
    }
}
