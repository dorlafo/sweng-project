package ch.epfl.sweng.jassatepfl.database.local.reference;

import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.database.helpers.QueryWrapper;
import ch.epfl.sweng.jassatepfl.database.local.Leaf;
import ch.epfl.sweng.jassatepfl.database.local.LeafField;
import ch.epfl.sweng.jassatepfl.database.local.MatchLeaf;
import ch.epfl.sweng.jassatepfl.database.local.MatchStatusLeaf;
import ch.epfl.sweng.jassatepfl.database.local.Node;
import ch.epfl.sweng.jassatepfl.database.local.PlayerLeaf;
import ch.epfl.sweng.jassatepfl.database.local.Root;
import ch.epfl.sweng.jassatepfl.database.local.TreeNode;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author Amaury Combes
 */
public class DBRefWrapMock extends DBReferenceWrapper {

    private Node currentNode;
    private static int numValueEventListener = 0;
    private static int numChildEventListener = 0;

    public DBRefWrapMock(DatabaseReference dbRef) {
        super();
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
        return new DBRefWrapMock(this.getCurrentNode().getChild(child));
    }

    @Override
    public DBReferenceWrapper push() {
        Node currentNode = this.getCurrentNode();
        currentNode.addAutoGeneratedChild();
        return this;
    }

    @Override
    public String getKey() {
        return this.getCurrentNode().getId();
    }

    /**
     * Look at the firebase documentation to see what this method does
     */
    @Override
    public Task<Void> setValue(Object value) {
        if(getCurrentNode() instanceof Leaf) {
            ((Leaf) getCurrentNode()).setData(value);
        } else if(getCurrentNode() instanceof LeafField) {
            ((LeafField) getCurrentNode()).setData(value);
        } else {
            throw new UnsupportedOperationException("Cannot apply setValue on node : " + getCurrentNode().getId());
        }

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
        List<Boolean> status = null;

        if(this.getCurrentNode() instanceof PlayerLeaf) {
            p = ((PlayerLeaf) this.getCurrentNode()).getData();
        } else if(this.getCurrentNode() instanceof MatchLeaf) {
            m = ((MatchLeaf) this.getCurrentNode()).getData();
        } else if(this.getCurrentNode() instanceof MatchStatusLeaf) {
            status = new ArrayList<>(((MatchStatusLeaf) this.getCurrentNode()).getData());
        }

        when(obj.getValue(Player.class)).thenReturn(p);
        when(obj.getValue(Match.class)).thenReturn(m);
        new Thread(new Runnable() {
            public void run() {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                Runnable toRun = new Runnable() {
                    @Override
                    public void run() {
                        v.onDataChange((DataSnapshot) obj);
                    }
                };
                uiHandler.post(toRun);
            }
        }).start();

    }

    /**
     * Look at the firebase documentation to see what this method does
     */
    /*@Override
    public ValueEventListener addValueEventListener(final ValueEventListener v) {
        final DataSnapshot obj = mock(DataSnapshot.class);
        Player p = null;
        Match m = null;
        List<Boolean> status = null;

        if(this.getCurrentNode() instanceof PlayerLeaf) {
            p = ((PlayerLeaf) this.getCurrentNode()).getData();
        } else if(this.getCurrentNode() instanceof MatchLeaf) {
            m = ((MatchLeaf) this.getCurrentNode()).getData();
        } else if(this.getCurrentNode() instanceof MatchStatusLeaf) {
            status = new ArrayList<>(((MatchStatusLeaf) this.getCurrentNode()).getData());
        }

        when(obj.getValue(Player.class)).thenReturn(p);
        when(obj.getValue(Match.class)).thenReturn(m);
        Thread t = new Thread(new Runnable() {
            public void run() {
                v.onDataChange((DataSnapshot) obj);
            }
        });
        t.start();
        return v;
    }*/

    /**
     * Look at the firebase documentation to see what this method does
     */
    @Override
    public ValueEventListener addValueEventListener(final ValueEventListener listener) {
        ++numValueEventListener;

        new Thread(new Runnable() {

            public void run() {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                Runnable toRun = new Runnable() {
                    @Override
                    public void run() {
                        Player p = null;
                        Match m = null;

                        while(numValueEventListener > 0) {
                            final DataSnapshot obj = mock(DataSnapshot.class);

                            List<Boolean> status = null;
                            boolean callDataChange = false;

                            if(currentNode instanceof PlayerLeaf) {
                                if(p == null || !p.equals(((PlayerLeaf) currentNode).getData())) {
                                    callDataChange = true;
                                }
                                p = ((PlayerLeaf) currentNode).getData();
                            } else if(currentNode instanceof MatchLeaf) {
                                if(m == null || !m.equals(((MatchLeaf) currentNode).getData())) {
                                    callDataChange = true;
                                }
                                m = ((MatchLeaf) currentNode).getData();
                            } else if(currentNode instanceof MatchStatusLeaf) {
                                status = new ArrayList<>(((MatchStatusLeaf) currentNode).getData());
                            }

                            when(obj.getValue(Player.class)).thenReturn(p);
                            when(obj.getValue(Match.class)).thenReturn(m);

                            if(callDataChange) {
                                listener.onDataChange((DataSnapshot) obj);
                            }
                    }
                }
                };
                uiHandler.post(toRun);
            }
        }).start();
        return listener;
    }

    /**
     * Look at the firebase documentation to see what this method does
     */
    @Override
    public ChildEventListener addChildEventListener(final ChildEventListener listener) {
        ++numChildEventListener;

        new Thread(new Runnable() {

            @Override
            public void run() {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                Runnable toRun = new Runnable() {
                    @Override
                    public void run() {
                                    final DataSnapshot snap = mock(DataSnapshot.class);

                                    if(currentNode instanceof MatchStatusLeaf) {
                                        List<Boolean> statusList = ((MatchStatusLeaf) currentNode).getData();
                                        for(int i = 0; i < statusList.size(); ++i) {
                                            boolean value = statusList.get(i);
                                            when(snap.getKey()).thenReturn(Integer.toString(i));
                                            when(snap.getValue()).thenReturn(value);
                                            listener.onChildAdded(snap, currentNode.getId());
                                        }

                                    }
                            }
                        };
                        uiHandler.post(toRun);
                    }
                }).start();
        return listener;
    }

    @Override
    public void removeEventListener(ChildEventListener listener) {
        --numChildEventListener;
    }

    @Override
    public void removeEventListener(ValueEventListener listener) {
        --numValueEventListener;
    }

    /**
     * Look at the firebase documentation to see what this method does
     */
    @Override
    public QueryWrapper orderByChild(String path) {
        List<Leaf> leafList = new ArrayList();
        String childOrder = null;
        for(Node n: currentNode.getChildren()) {
            Leaf l = ((Leaf) n);
            leafList.add(l);
        }

        if(path.equals("firstName")) {
            Collections.sort(leafList, new Comparator<Leaf>() {
                @Override
                public int compare(Leaf l1, Leaf l2) {
                    return ((Player)l1.getData()).getFirstName().compareTo(((Player)l2.getData()).getFirstName());
                }
            });
            childOrder = "firstName";
            return new QueryWrapperMock(leafList, childOrder);
        } else if(path.equals("privateMatch")) {
            Collections.sort(leafList, new Comparator<Leaf>() {
                @Override
                public int compare(Leaf o1, Leaf o2) {
                    if((((Match) o1.getData()).isPrivateMatch() &&
                            ((Match) o2.getData()).isPrivateMatch()) ||
                            (!((Match) o1.getData()).isPrivateMatch() &&
                                    !((Match) o2.getData()).isPrivateMatch())) {
                        return 0;
                    } else if(!((Match) o1.getData()).isPrivateMatch() &&
                                ((Match) o2.getData()).isPrivateMatch()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
            childOrder = "privateMatch";
            return new QueryWrapperMock(leafList, childOrder);
        }

        throw new IllegalArgumentException("Path : " + path + " is not supported");
    }

    @Override
    public void removeValue() {

    }

    /**
     * Drop all children of the currentNode. This can be used as a reset of the local database
     */
    public void reset() {
        currentNode.dropChildren();
        currentNode.initialize();
    }

    /**
     * addPlayers let you fill the local database with the players you want
     * Be advise : this method should only be applied to a DBRefWrapMock that is currently pointing
     * to the root of the local database
     *
     * @param players the collection of players that needs to be added
     */
    public void addPlayers(Set<Player> players) {
        TreeNode playersNode = ((Root) currentNode).getChild("players");
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
        TreeNode playersNode = ((Root) currentNode).getChild("matches");
        for (Match m : matches) {
            String matchID = m.getMatchID();
            playersNode.addChild(matchID);
            playersNode.getChild(matchID).setData(m);
        }
    }

    public void addPendingMatch(Match match, List<Boolean> status) {
        TreeNode pendingMatch = ((Root) currentNode).getChild("pendingMatches");
        MatchStatusLeaf statusLeaf = (MatchStatusLeaf) pendingMatch.addChild(match.getMatchID().toString());
        statusLeaf.setData(status);
    }
}
