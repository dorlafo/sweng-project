package ch.epfl.sweng.project.database.tools;


import java.util.Collections;
import java.util.Set;

public class Leaf implements Node {
    private String id;
    private Object data;

    public Leaf(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Set<Node> getChildren() {
        return Collections.emptySet();
    }

    public Node getChild(String id) {
        return null;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void addAutoGeneratedChild() {
        throw new UnsupportedOperationException("Leaf does not support addAutoGeneratedChild()");
    }
}
