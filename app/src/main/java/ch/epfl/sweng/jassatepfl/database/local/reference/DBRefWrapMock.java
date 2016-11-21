package ch.epfl.sweng.jassatepfl.database.local.reference;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.database.local.Leaf;
import ch.epfl.sweng.jassatepfl.database.local.Node;
import ch.epfl.sweng.jassatepfl.database.local.Root;
import ch.epfl.sweng.jassatepfl.database.local.TreeNode;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Amaury Combes
 */
public class DBRefWrapMock extends DBReferenceWrapper {

    private Node currentNode;

    public DBRefWrapMock(DatabaseReference dbRef) {
        super(dbRef);
        currentNode = new Root("JassDB (Local mock database)");
    }

    public DBRefWrapMock(Node nodeToPoint) {
        super();
        this.currentNode = nodeToPoint;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    @Override
    public DBReferenceWrapper child(String child) {
        return null;
    }

    @Override
    public DBReferenceWrapper push() {
        return this;
    }

    @Override
    public String getKey() {
        return null;
    }

    /**
     * Look at the firebase documentation to see what this method does
     */
    @Override
    public Task<Void> setValue(Object value) {
        return null;
    }

    /**
     * Look at the firebase documentation to see what this method does
     */
    @Override
    public void addListenerForSingleValueEvent(final ValueEventListener v) {
        final DataSnapshot obj = mock(DataSnapshot.class);
        Player p = null;
        Match m = null;

        if (((Leaf) this.getCurrentNode()).getData() instanceof Player) {
            p = (Player) ((Leaf) this.getCurrentNode()).getData();
        } else if (((Leaf) this.getCurrentNode()).getData() instanceof Match) {
            m = (Match) ((Leaf) this.getCurrentNode()).getData();
        }

        when(obj.getValue(Player.class)).thenReturn(p);
        when(obj.getValue(Match.class)).thenReturn(m);
        Thread t = new Thread(new Runnable() {
            public void run() {
                v.onDataChange((DataSnapshot) obj);
            }
        });
        t.start();

    }

    /**
     * Look at the firebase documentation to see what this method does
     */
    @Override
    public ChildEventListener addChildEventListener(ChildEventListener listener) {
        return null;
    }

    /**
     * Drop all children of the currentNode. This can be used as a reset of the local database
     */
    public void reset() {
        currentNode.dropChildren();
        //TODO: rename
        currentNode.addChild("players2");
        //TODO: rename
        currentNode.addChild("matches2");
    }

    /**
     * addPlayers let you fill the local database with the players you want
     * Be advise : this method should only be applied to a DBRefWrapMock that is currently pointing
     * to the root of the local database
     *
     * @param players the collection of players that needs to be added
     */
    public void addPlayers(Set<Player> players) {
        //TODO: rename
        TreeNode playersNode = ((Root) currentNode).getChild("players2");
        for (Player p : players) {
            String playerId = p.getID().toString();
            playersNode.addChild(playerId);
            playersNode.getChild(playerId).setData(p);
        }
    }

    /**
     * addMatches let you fill the local database with the matches you want
     * Be advise : this method should only be applied to a DBRefWrapMock that is currently pointing
     * to the root of the local database
     *
     * @param matches the collection of players that needs to be added
     */
    public void addMatches(Set<Match> matches) {
        //TODO: rename
        TreeNode playersNode = ((Root) currentNode).getChild("matches2");
        for (Match m : matches) {
            String matchID = m.getMatchID();
            playersNode.addChild(matchID);
            playersNode.getChild(matchID).setData(m);
        }
    }

}
