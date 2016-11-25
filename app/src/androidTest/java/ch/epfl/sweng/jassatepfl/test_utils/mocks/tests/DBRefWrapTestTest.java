package ch.epfl.sweng.jassatepfl.test_utils.mocks.tests;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.MatchStatusLeafTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.RootTest;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.mocks.DBRefWrapTest;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

import static org.junit.Assert.assertEquals;


/**
 * @author Amaury Combes
 */
public class DBRefWrapTestTest {
    List<Player> playerList;

    @Test
    public void valueEventListenerOnMatchTest() {
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

        DBRefWrapTest refToPrivate = (DBRefWrapTest) localRef.child(DatabaseUtils.DATABASE_MATCHES).child(DatabaseUtils.DATABASE_MATCHES_PRIVATE);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Match match = dataSnapshot.getValue(Match.class);
                playerList = new ArrayList<>(match.getPlayers());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        refToPrivate.addValueEventListener(listener);
        waitCompletion();
        refToPrivate.removeEventListener(listener);
        assertEquals(playerList.get(0), DummyDataTest.jimmy);
    }

    @Test
    public void setValueOnStatusPendingMatchTest() {
        RootTest root = new RootTest("jass@Epfl");
        root.addChild(DatabaseUtils.DATABASE_MATCHES);
        root.addChild(DatabaseUtils.DATABASE_PLAYERS);
        root.addChild(DatabaseUtils.DATABASE_PENDING_MATCHES);
        DBRefWrapTest localRef = new DBRefWrapTest(root);

        Set<Player> players = new HashSet<>();
        players.add(DummyDataTest.amaury);
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.privateMatch());
        localRef.addPlayers(players);
        localRef.addMatches(matches);
        List<Boolean> status = Arrays.asList(true, false, false ,false);
        localRef.addPendingMatch(DummyDataTest.privateMatch(), status);
        DBRefWrapTest refToMatchStatus = (DBRefWrapTest) localRef.child(DatabaseUtils.DATABASE_PENDING_MATCHES).child(DummyDataTest.privateMatch().getMatchID().toString());
        refToMatchStatus.child("0").setValue(false);
        refToMatchStatus.child("1").setValue(true);

        waitCompletion();

        assertEquals(false, ((MatchStatusLeafTest) refToMatchStatus.getCurrentNode()).getData().get(0));
        assertEquals(true, ((MatchStatusLeafTest) refToMatchStatus.getCurrentNode()).getData().get(1));
    }

    private void waitCompletion() {
        try {
            Thread.sleep(4000);
        } catch (Exception e){
            throw new Error("Something went wrong");
        }
    }
}
