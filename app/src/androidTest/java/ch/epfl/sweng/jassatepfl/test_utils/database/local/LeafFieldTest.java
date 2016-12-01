package ch.epfl.sweng.jassatepfl.test_utils.database.local;


import java.util.Set;

/**
 * @author Amaury Combes
 */
public class LeafFieldTest<T> implements NodeTest {
    private final String id;
    private T data;
    private NodeTest parent;

    public LeafFieldTest(String id, NodeTest parent) {
        this.id = id;
        this.parent = parent;
    }

    public LeafFieldTest(String id, T data) {
        this.id = id;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Set<NodeTest> getChildren() {
        throw new UnsupportedOperationException("LeafFieldTest does not support getChildren()");
    }

    @Override
    public NodeTest getParent() {
        throw new UnsupportedOperationException("LeafFieldTest does not support getParent()");
    }

    @Override
    public NodeTest getChild(String id) throws IllegalArgumentException {
        throw new UnsupportedOperationException("LeafFieldTest does not support getChild()");
    }

    @Override
    public NodeTest addAutoGeneratedChild() {
        throw new UnsupportedOperationException("LeafFieldTest does not support addAutoGeneratedChild()");
    }

    @Override
    public NodeTest addChild(String id) {
        throw new UnsupportedOperationException("LeafFieldTest does not support addChild()");
    }

    @Override
    public void dropChildren() {
        throw new UnsupportedOperationException("LeafFieldTest does not support dropChildren()");
    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("LeafFieldTest does not support initialize()");
    }

    @Override
    public void removeSelf() {
        if(data instanceof Boolean) {
            ((MatchStatusLeafTest) parent).getStatus().remove(id);
        }
    }

    @Override
    public void removeChild(NodeTest child) {
        throw new UnsupportedOperationException("LeafFieldTest does not support removeChild()");

    }
}
