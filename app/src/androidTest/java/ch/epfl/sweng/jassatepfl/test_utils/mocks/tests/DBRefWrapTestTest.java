package ch.epfl.sweng.jassatepfl.test_utils.mocks.tests;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.PendingMatchLeafTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.RootTest;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.mocks.DBRefWrapTest;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.amaury;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.dorian;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.jimmy;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.vincenzo;
import static org.junit.Assert.assertEquals;


/**
 * @author Amaury Combes
 */
public class DBRefWrapTestTest {
    List<Player> playerList;

    @Test
    public void valueEventListenerOnMatchTest() {
        RootTest root = new RootTest("jass@EPFL");
        root.initialize();

        DBRefWrapTest localRef = new DBRefWrapTest(root);

        Set<Player> players = new HashSet<>();
        players.add(amaury);
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.privateMatch());
        localRef.addPlayers(players);
        localRef.addMatches(matches);

        DBRefWrapTest refToPrivate = (DBRefWrapTest) localRef.child(DatabaseUtils.DATABASE_MATCHES).child("private"); //Here "private" is the matchID of the match
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
        refToPrivate.removeEventListener(listener);
        assertEquals(playerList.get(0), jimmy);
    }

    @Test
    public void setValueOnStatusPendingMatchTest() {
        RootTest root = new RootTest("jass@EPFL");
        root.initialize();

        DBRefWrapTest localRef = new DBRefWrapTest(root);

        Set<Player> players = new HashSet<>();
        players.add(vincenzo);
        players.add(dorian);
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.twoPlayersMatch());
        localRef.addPlayers(players);
        localRef.addMatches(matches);
        Map<String, Boolean> status = new HashMap<>();
        status.put(dorian.getID().toString(), true);
        status.put(vincenzo.getID().toString(), false);
        localRef.addPendingMatch(DummyDataTest.twoPlayersMatch(), status);
        DBRefWrapTest refToMatchStatus = (DBRefWrapTest) localRef.child(DatabaseUtils.DATABASE_PENDING_MATCHES).child(DummyDataTest.twoPlayersMatch().getMatchID().toString());
        refToMatchStatus.child(dorian.getID().toString()).setValue(false);
        refToMatchStatus.child(vincenzo.getID().toString()).setValue(true);

        assertEquals(false, ((PendingMatchLeafTest) refToMatchStatus.getCurrentNode()).getData().get(dorian.getID().toString()));
        assertEquals(true, ((PendingMatchLeafTest) refToMatchStatus.getCurrentNode()).getData().get(vincenzo.getID().toString()));
    }

    //TODO: Make this test actually test something...
    @Test
    public void childEventListenerTest() {
        RootTest root = new RootTest("jass@EPFL");
        root.initialize();

        DBRefWrapTest localRef = new DBRefWrapTest(root);

        localRef.addPlayers(DummyDataTest.players());

        ChildEventListener cel = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Player p = dataSnapshot.getValue(Player.class);
                Log.d("cel", "onChildAdded:player:" + p.getFirstName());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Player p = dataSnapshot.getValue(Player.class);
                Log.d("cel", "onChildChanged:player:" + p.getFirstName());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Player p = dataSnapshot.getValue(Player.class);
                Log.d("cel", "onChildRemoved:player:" + p.getFirstName());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Player p = dataSnapshot.getValue(Player.class);
                Log.d("cel", "onChildMoved:player:" + p.getFirstName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        localRef.child("players").addChildEventListener(cel);
    }
}
