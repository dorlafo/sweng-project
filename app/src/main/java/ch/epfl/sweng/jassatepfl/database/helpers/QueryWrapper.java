package ch.epfl.sweng.jassatepfl.database.helpers;


import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * @author Amaury Combes
 *
 */
public class QueryWrapper {
    private Query query;

    public QueryWrapper() {
        this.query = null;
    }

    public QueryWrapper(Query query) {
        this.query = query;
    }

    public QueryWrapper startAt(String path) {
        return new QueryWrapper(query.startAt(path));
    }

    public QueryWrapper endAt(String path) {
        return new QueryWrapper(query.endAt(path));
    }

    public QueryWrapper limitToFirst(int num) {
        return new QueryWrapper(query.limitToFirst(num));
    }

    public ValueEventListener addValueEventListener(ValueEventListener listener) {
        return query.addValueEventListener(listener);
    }
}
