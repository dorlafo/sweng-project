package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

/**
 * The TreeNodeTest class is a special case of the NodeTest interface. It represents the middle nodes of our
 * tree structure
 *
 */
public class TreeNodeTest extends NodeTest {

    private String id;
    private Set<NodeTest> children;
    private NodeTest parent;

    /**
     * Constructor of the TreeNodeTest class
     *
     * @param id the id of the TreeNodeTest that is created
     */
    public TreeNodeTest(String id, NodeTest parent) {
        this.id = id;
        children = new HashSet<>();
        this.parent = parent;
        this.isDeleted = false;
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
    public void setData(Object data) {
        throw new UnsupportedOperationException("TreeNode does not support setData");
    }

    @Override
    public NodeTest getChild(String id) {
        for (NodeTest n : children) {
            if (n.getId().equals(id)) {
                return n;
            }
        }
        return this.addChild(id);
    }

    @Override
    public LeafTest addChild(String id) {
        if(isDeleted) {
            this.isDeleted = false;
        }

        LeafTest deletedLeaf = null;
        for(NodeTest n : children) {
            if(n.getId().equals(id) && n.isDeleted) {
                deletedLeaf = (LeafTest) n;
            }
        }
        if(deletedLeaf != null) {
            deletedLeaf.dropChildren();
            deletedLeaf.deleteAllObservers();
            children.remove(deletedLeaf);
        }

        LeafTest newLeaf;

        switch (this.id) {
            case DatabaseUtils.DATABASE_PLAYERS:
                newLeaf = new PlayerLeafTest(id, this);
                break;
            case DatabaseUtils.DATABASE_MATCHES:
                newLeaf = new MatchLeafTest(id, this);
                break;
            case DatabaseUtils.DATABASE_PENDING_MATCHES:
                newLeaf = new PendingMatchLeafTest(id, this);
                break;
            case DatabaseUtils.DATABASE_MATCH_STATS:
                newLeaf = new MatchStatsLeafTest(id, this);
                break;
            case DatabaseUtils.DATABASE_USERSTATS:
                newLeaf = new UserStatsLeafTest(id, this);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        children.add(newLeaf);
        this.setAdded();
        this.notifyObservers(this);
        return newLeaf;
    }

    @Override
    public LeafTest addAutoGeneratedChild() {
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

        return this.addChild(tempId);
    }

    @Override
    public void dropChildren() {
        if(!isDeleted) {
            for(NodeTest n : children) {
                n.dropChildren();
                n.deleteAllObservers();
            }
            children = new HashSet<>();
        }
    }

    private String randomStringGenerator() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void removeSelf() {
        this.isDeleted = true;
        for(NodeTest n : children) {
            removeChild(n);
        }
        this.setDeleted();
        this.notifyObservers(this);
    }

    @Override
    public void removeChild(NodeTest child) {
        for (NodeTest n : children) {
            if (n.getId().equals(child.getId()) && !n.isDeleted) {
                n.removeSelf();
                this.setChanged();
                this.notifyObservers(this);
            }
        }
    }
}
