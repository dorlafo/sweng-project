package ch.epfl.sweng.jassatepfl.test_utils.mocks;

import android.os.Handler;
import android.os.Looper;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.jassatepfl.database.helpers.QueryWrapper;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.LeafTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Amaury Combes
 */
public class QueryWrapperMockTest extends QueryWrapper {
    private final List<LeafTest> elements;
    private String childOrder;
    private int numOfValueListener = 0;
    private int numOfChildListener = 0;

    public QueryWrapperMockTest(List<LeafTest> elems, String childOrder) {
        super();
        elements = new ArrayList<>(elems);
        this.childOrder = childOrder;
    }

    private QueryWrapperMockTest(List<LeafTest> elems, int numOfValueListener, int numOfChildListener) {
        super();
        elements = new ArrayList<>(elems);
        this.numOfValueListener = numOfValueListener;
        this.numOfChildListener = numOfChildListener;
    }

    @Override
    public QueryWrapper startAt(String path) {
        int i = 0;
        int listSize = elements.size();
        List<LeafTest> elems = new ArrayList<>(elements);
        for (LeafTest l : elems) {
            if (!l.getId().startsWith(path)) elems.remove(l);
        }
        return new QueryWrapperMockTest(elems, numOfValueListener, numOfChildListener);
    }

    @Override
    public QueryWrapper endAt(String path) {
        return new QueryWrapperMockTest(elements, numOfValueListener, numOfChildListener);
    }

    @Override
    public QueryWrapper limitToFirst(int num) {
        return new QueryWrapperMockTest(elements.subList(0, num - 1), numOfValueListener, numOfChildListener);
    }

    @Override
    public QueryWrapper equalTo(Boolean b) {
        List<LeafTest> newLeafs = new ArrayList<>();
        for (LeafTest l : elements) {
            Match p = (Match) l.getData();
            if (p.isPrivateMatch() == b) {
                newLeafs.add(l);
            }
        }
        return new QueryWrapperMockTest(newLeafs, numOfValueListener, numOfChildListener);
    }

    @Override
    public void removeEventListener(ValueEventListener listener) {
        --numOfValueListener;
    }

    @Override
    public void removeEventListener(ChildEventListener listener) {
        --numOfChildListener;
    }

    @Override
    public ValueEventListener addValueEventListener(final ValueEventListener listener) {
        ++numOfValueListener;
        new Thread() {
            public void run() {
                for (LeafTest l : elements) {
                    Player p = null;
                    Match m = null;
                    DataSnapshot dSnap = mock(DataSnapshot.class);
                    Object obj = l.getData();
                    if (obj instanceof Player) {
                        p = (Player) obj;
                    }

                    when(dSnap.getValue(Player.class)).thenReturn(p);
                    when(dSnap.getValue(Match.class)).thenReturn(m);

                    listener.onDataChange(dSnap);
                }
            }
        }.start();
        return listener;
    }

    @Override
    public ChildEventListener addChildEventListener(final ChildEventListener listener) {
        ++numOfChildListener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (final LeafTest l : elements) {

                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    Runnable toRun = new Runnable() {
                        @Override
                        public void run() {
                            Player p = null;
                            Match m = null;
                            String id = null;

                            DataSnapshot dSnap = mock(DataSnapshot.class);
                            Object obj = l.getData();
                            if (obj instanceof Player) {
                                p = (Player) obj;
                                id = p.getID().toString();
                            } else if (obj instanceof Match) {
                                m = (Match) obj;
                                id = m.getMatchID();
                            }

                            when(dSnap.getValue(Player.class)).thenReturn(p);
                            when(dSnap.getValue(Match.class)).thenReturn(m);
                            listener.onChildAdded(dSnap, id);
                        }
                    };
                    uiHandler.post(toRun);
                }
            }
        }).start();
        return listener;
    }
}
