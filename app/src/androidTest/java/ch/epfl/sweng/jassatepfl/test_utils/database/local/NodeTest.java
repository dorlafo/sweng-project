package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.Set;

/**
 * The NodeTest interface represents a link to an element of the local database used for tests
 * This interface is implemented by multiple class and some operations might not be supported
 * in all of them. This is because the data structure has some specific architecture (i.e. a three
 * of height 3).
 *
 * @author Amaury Combes
 */
public interface NodeTest<T> {

    /**
     * Getter for the id of the current node
     *
     * @return the id of the current node
     */
    String getId();

    /**
     * Getter for the children of the current node
     *
     * @return the children of the current node
     */
    Set<NodeTest> getChildren();

    NodeTest getParent();

    /**
     * Tries to get a specific child of the current node
     *
     * @param id the id of child to get
     * @return the node with the corresponding id
     * @throws IllegalArgumentException if the id passed as argument does not exists
     */
    NodeTest getChild(String id) throws IllegalArgumentException;

    /**
     * Adds a child with a random id
     *
     * @return the node added
     */
    NodeTest addAutoGeneratedChild();

    /**
     * Add a child to the current node
     *
     * @param id the id of the child to add
     * @return the child that was just added
     */
    NodeTest addChild(String id);

    void setData(T data);

    /**
     * Remove the current node from the database
     */
    void removeSelf();

    void removeChild(NodeTest child);

    /**
     * Remove all children of the current node
     */
    void dropChildren();

    void initialize();
}
