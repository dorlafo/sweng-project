package ch.epfl.sweng.jassatepfl.database.local;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The TreeNode class is a special case of the Node interface. It represents the middle nodes of our
 * tree structure
 *
 * @author Amaury Combes
 */
public class TreeNode implements Node {

    private String id;
    private Set<Node> children;

    /**
     * Constructor of the TreeNode class
     *
     * @param id the id of the TreeNode that is created
     */
    public TreeNode(String id) {
        this.id = id;
        children = new HashSet<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Set<Node> getChildren() {
        return new HashSet<>(children);
    }

    @Override
    public Leaf getChild(String id) {
        for (Node n : children) {
            if (n.getId().equals(id)) {
                return (Leaf) n;
            }
        }
        throw new IllegalArgumentException("The node does not have a children named : " + id);
    }

    @Override
    public Leaf addChild(String id) {
        Leaf newLeaf = new Leaf(id);
        children.add(newLeaf);
        return newLeaf;
    }

    @Override
    public Leaf addAutoGeneratedChild() {
        String tempId = randomStringGenerator();
        boolean safeCopy = false;

        while (!safeCopy) {
            safeCopy = true;
            for (Node n : children) {
                if (n.getId().equals(tempId)) {
                    safeCopy = false;
                    tempId = randomStringGenerator();
                }
            }
        }

        Leaf newLeaf = new Leaf(tempId);
        children.add(newLeaf);
        return newLeaf;
    }

    @Override
    public void dropChildren() {
        children = new HashSet<>();
    }

    private String randomStringGenerator() {
        return UUID.randomUUID().toString();
    }

}