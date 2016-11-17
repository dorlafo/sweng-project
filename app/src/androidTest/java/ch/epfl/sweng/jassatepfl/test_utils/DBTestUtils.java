package ch.epfl.sweng.jassatepfl.test_utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ch.epfl.sweng.jassatepfl.database.local.reference.DBRefWrapMock;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexis Montavon
 *
 * Utils class for testing DB
 */
public class DBTestUtils {

    public static void assertMatchContainsNPlayers(DBRefWrapMock dbRefWrapMock, String matchID, final int nPlayers) {
        dbRefWrapMock.child("matches")
                .child(matchID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Match match = dataSnapshot.getValue(Match.class);
                assertTrue(match.getPlayers().size() == nPlayers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void assertMatchContainsPlayer(DBRefWrapMock dbRefWrapMock, String matchID, final Player.PlayerID sciper) {
        dbRefWrapMock.child("matches")
                .child(matchID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Match match = dataSnapshot.getValue(Match.class);
                List<Player> playerList = match.getPlayers();
                for(Player p: playerList) {
                    if(p.getID().equals(sciper)) {
                        return;
                    }
                }
                fail();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
