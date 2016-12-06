package ch.epfl.sweng.jassatepfl.test_utils.database.local;


import java.util.Observable;
import java.util.Set;

/**
 * The leaf class is a particular case of the NodeTest interface. It represents the bottom of our
 * database.
 * Some operations are not supported
 *
 * @author Amaury Combes
 */
public abstract class LeafTest<T> extends Observable implements NodeTest {

    private String id;
    protected T data;

    private NodeTest parent;

    protected boolean deleted;

    /**
     * Constructor of the LeafTest class
     *
     * @param id the id of the LeafTest that is created
     */
    public LeafTest(String id, NodeTest parent) {
        this.id = id;
        this.parent = parent;
        this.deleted = false;
    }

    @Override
    public NodeTest getParent() {
        return parent;
    }

    /**
     * Setter for the data of the current leaf
     *
     * @param data the data that we need to add
     */
    public abstract void setData(T data);

    @Override
    public abstract LeafFieldTest getChild(String id);

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Set<NodeTest> getChildren() {
        throw new UnsupportedOperationException("LeafTest does not support getChildren()");
    }

    @Override
    public NodeTest addChild(String id) {
        throw new UnsupportedOperationException("LeafTest does not support addChild(id)");
    }

    @Override
    public NodeTest addAutoGeneratedChild() {
        throw new UnsupportedOperationException("LeafTest does not support addAutoGeneratedChild()");
    }

    /**
     * Getter for the data of the current leaf
     *
     * @return the data of the current leaf
     */
    public T getData() {
        return data;
    }


    @Override
    public void dropChildren() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void removeChild(NodeTest child) {
        throw new UnsupportedOperationException("LeafTest does not support removeChild()");
    }

    @Override
    public void removeSelf() {
        parent.removeChild(this);
    }

    public boolean isDeleted() {
        return deleted;
    }
}
