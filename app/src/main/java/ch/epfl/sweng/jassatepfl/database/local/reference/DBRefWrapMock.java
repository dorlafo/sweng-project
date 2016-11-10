package ch.epfl.sweng.jassatepfl.database.local.reference;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.database.local.Node;
import ch.epfl.sweng.jassatepfl.database.local.Root;

import static org.mockito.ArgumentMatchers.any;

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
    public void addListenerForSingleValueEvent(ValueEventListener listener) {

    }

    /**
     * Look at the firebase documentation to see what this method does
     */
    @Override
    public ChildEventListener addChildEventListener(ChildEventListener listener) {
        return null;
    }
}
