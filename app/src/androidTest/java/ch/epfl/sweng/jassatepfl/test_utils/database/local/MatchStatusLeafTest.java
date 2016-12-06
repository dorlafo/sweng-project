package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Amaury Combes
 */
public class MatchStatusLeafTest extends LeafTest<Map<String, Boolean>> {

    private Map<String, LeafFieldTest<Boolean>> status = new HashMap<>();

    public MatchStatusLeafTest(String id, NodeTest parent) {
        super(id, parent);
    }

    @Override
    public LeafFieldTest getChild(String id) {
        if(status.containsKey(id)) {
            return status.get(id);
        }
        else {
            throw new IllegalArgumentException("Could not get player : " + id);
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
        setChanged();
        notifyObservers(this);
    }

    @Override
    public void setData(Map<String, Boolean> data) {
        if(deleted) {
            throw new UnsupportedOperationException("Cannot setData on a deleted MatchStatusLeafTest");
        }
        this.data = data;
        for(String key : data.keySet()) {
            LeafFieldTest<Boolean> s = new LeafFieldTest<>(key, this);
            s.setData(data.get(key));
            status.put(key, s);
        }
        setChanged();
        notifyObservers(this);
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
