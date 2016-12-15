package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

/**
 * @author Amaury Combes
 */
public class PendingMatchLeafTest extends LeafTest<Map<String, Boolean>> {

    private Map<String, LeafFieldTest<Boolean>> status = new HashMap<>();

    public PendingMatchLeafTest(String id, NodeTest parent) {
        super(id, parent);
    }

    @Override
    public LeafFieldTest getChild(String id) {
        if(status.containsKey(id)) {
            return status.get(id);
        }
        else {
            throw new IllegalArgumentException("Could not get pending match value for player : " + id);
        }
    }

    public Map<String, LeafFieldTest<Boolean>> getStatus() {
        return status;
    }

    public void removeOneStatus(String id) {
        status.remove(id);
        if(status.isEmpty()) {
            removeSelf();
        }
        this.setChanged();
        this.notifyObservers(this);
    }

    @Override
    public void setData(Map<String, Boolean> data) {
        if(isDeleted) {
            isDeleted = true;
            //throw new UnsupportedOperationException("Cannot setData on a deleted PendingMatchLeafTest");
        }
        this.data = data;
        for(String key : data.keySet()) {
            LeafFieldTest<Boolean> s = new LeafFieldTest<>(key, this);
            s.setData(data.get(key));
            status.put(key, s);
        }
        this.setChanged();
        this.notifyObservers(this);
    }

    @Override
    public Map<String, Boolean> getData() {
        Map<String, Boolean> data = new HashMap<>();
        for(String key : status.keySet()) {
            data.put(key, status.get(key).getData());
        }
        return data;
    }
}
