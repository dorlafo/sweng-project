package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.UUID;

import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

/**
 * The TreeNodeTest class is a special case of the NodeTest interface. It represents the middle nodes of our
 * tree structure
 *
 * @author Amaury Combes
 */
public class TreeNodeTest extends NodeTest {

    private String id;
    private Set<NodeTest> children;
    private NodeTest parent;
    protected boolean deleted;

    /**
     * Constructor of the TreeNodeTest class
     *
     * @param id the id of the TreeNodeTest that is created
     */
    public TreeNodeTest(String id, NodeTest parent) {
        this.id = id;
        children = new HashSet<>();
        this.parent = parent;
        this.deleted = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Set<NodeTest> getChildren() {
        return new HashSet<>(children);
    }

    @Override
    public NodeTest getParent() {
        return parent;
    }

    @Override
    public LeafTest getChild(String id) {
        for (NodeTest n : children) {
            if (n.getId().equals(id)) {
                return (LeafTest) n;
            }
        }
        throw new IllegalArgumentException("The node '" + this.id + "' does not have a children named : " + id);
    }

    @Override
    public LeafTest addChild(String id) {
        if(deleted) {
            throw new UnsupportedOperationException("Cannot add a child to the deleted TreeNodeTest : " + id);
        }
        LeafTest deletedLeaf = null;
        for(NodeTest n : children) {
            if(n.getId().equals(id) && ((LeafTest)n).deleted) {
                deletedLeaf = (LeafTest) n;
            }
        }
        if(deletedLeaf != null) {
            children.remove(deletedLeaf);
        }
        switch (this.id) {
            case DatabaseUtils.DATABASE_PLAYERS:
                PlayerLeafTest playerLeaf = new PlayerLeafTest(id, this);
                children.add(playerLeaf);
                return playerLeaf;
            case DatabaseUtils.DATABASE_MATCHES:
                MatchLeafTest matchLeaf = new MatchLeafTest(id, this);
                children.add(matchLeaf);
                return matchLeaf;
            case DatabaseUtils.DATABASE_PENDING_MATCHES:
                MatchStatusLeafTest statusLeaf = new MatchStatusLeafTest(id, this);
                children.add(statusLeaf);
                return statusLeaf;
            case DatabaseUtils.DATABASE_MATCH_STATS:
                MatchStatsLeafTest statsLeaf = new MatchStatsLeafTest(id, this);
                children.add(statsLeaf);
                return statsLeaf;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public LeafTest addAutoGeneratedChild() {
        if(deleted) {
            throw new UnsupportedOperationException("Cannot add an auto generated child to the deleted TreeNodeTest : " + id);
        }
        String tempId = randomStringGenerator();
        boolean safeCopy = false;

        while (!safeCopy) {
            safeCopy = true;
            for (NodeTest n : children) {
                if (n.getId().equals(tempId)) {
                    safeCopy = false;
                    tempId = randomStringGenerator();
                }
            }
        }

        LeafTest newLeaf;
        switch (this.id) {
            case DatabaseUtils.DATABASE_PLAYERS:
                newLeaf = new PlayerLeafTest(tempId, this);
                break;
            case DatabaseUtils.DATABASE_MATCHES:
                newLeaf = new MatchLeafTest(tempId, this);
                break;
            case DatabaseUtils.DATABASE_PENDING_MATCHES:
                newLeaf = new MatchStatusLeafTest(tempId, this);
                break;
            case DatabaseUtils.DATABASE_MATCH_STATS:
                newLeaf = new MatchStatsLeafTest(tempId, this);
                break;
            default:
                throw new UnsupportedOperationException("Cannot add an auto generated child to : " + id);
        }
        children.add(newLeaf);
        return newLeaf;
    }

    @Override
    public void dropChildren() {
        if(deleted) {
            throw new UnsupportedOperationException("Cannot call dropChildren() on the deleted node : " + id);
        }
        children = new HashSet<>();
    }

    private String randomStringGenerator() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void removeSelf() {
        parent.removeChild(this);
    }

    @Override
    public void removeChild(NodeTest child) {
        for (NodeTest n : children) {
            if (n.getId().equals(child.getId())) {
                ((LeafTest) n).deleted = true;
            }
        }
    }

}
