package ch.epfl.sweng.jassatepfl.mocks;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.database.local.Leaf;
import ch.epfl.sweng.jassatepfl.database.local.MatchStatusLeaf;
import ch.epfl.sweng.jassatepfl.database.local.Root;
import ch.epfl.sweng.jassatepfl.database.local.reference.DBRefWrapMock;
import ch.epfl.sweng.jassatepfl.database.local.reference.QueryWrapperMock;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.DummyData;

import static org.junit.Assert.assertEquals;


/**
 * @author Amaury Combes
 */
public class DBRefWrapMockTest {
    List<Player> playerList;

    @Test
    public void valueEventListenerOnMatchTest() {
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

        DBRefWrapMock refToPrivate = (DBRefWrapMock) localRef.child("matches").child("private");
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
        assertEquals(playerList.get(0), DummyData.jimmy);
    }

    @Test
    public void setValueOnStatusPendingMatchTest() {
        Root root = new Root("jass@Epfl");
        root.addChild("matches");
        root.addChild("players");
        root.addChild("pendingMatches");
        DBRefWrapMock localRef = new DBRefWrapMock(root);

        Set<Player> players = new HashSet<>();
        players.add(DummyData.amaury);
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.privateMatch());
        localRef.addPlayers(players);
        localRef.addMatches(matches);
        List<Boolean> status = Arrays.asList(true, false, false ,false);
        localRef.addPendingMatch(DummyData.privateMatch(), status);
        DBRefWrapMock refToMatchStatus = (DBRefWrapMock) localRef.child("pendingMatches").child(DummyData.privateMatch().getMatchID().toString());
        refToMatchStatus.child("0").setValue(false);
        refToMatchStatus.child("1").setValue(true);

        waitCompletion();

        assertEquals(false, ((MatchStatusLeaf) refToMatchStatus.getCurrentNode()).getData().get(0));
        assertEquals(true, ((MatchStatusLeaf) refToMatchStatus.getCurrentNode()).getData().get(1));
    }

    private void waitCompletion() {
        try {
            Thread.sleep(4000);
        } catch (Exception e){
            throw new Error("Something went wrong");
        }
    }
}
