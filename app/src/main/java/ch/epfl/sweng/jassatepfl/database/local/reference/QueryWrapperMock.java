package ch.epfl.sweng.jassatepfl.database.local.reference;

import android.os.Handler;
import android.os.Looper;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Field;

import ch.epfl.sweng.jassatepfl.database.helpers.QueryWrapper;
import ch.epfl.sweng.jassatepfl.database.local.Leaf;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Amaury Combes
 */
public class QueryWrapperMock extends QueryWrapper{
    private final List<Leaf> elements;
    private String childOrder;
    private int numOfValueListener = 0;
    private int numOfChildListener = 0;

    public QueryWrapperMock(List<Leaf> elems, String childOrder) {
        super();
        elements = new ArrayList<>(elems);
        this.childOrder = childOrder;
    }

    private QueryWrapperMock(List<Leaf> elems, int numOfValueListener, int numOfChildListener) {
        super();
        elements = new ArrayList<>(elems);
        this.numOfValueListener = numOfValueListener;
        this.numOfChildListener = numOfChildListener;
    }

    @Override
    public QueryWrapper startAt(String path) {
        int i = 0;
        int listSize = elements.size();
        List<Leaf> elems = new ArrayList<>(elements);
        for(Leaf l: elems) {
            if(!l.getId().startsWith(path)) elems.remove(l);
        }
        return new QueryWrapperMock(elems, numOfValueListener, numOfChildListener);
    }

    @Override
    public QueryWrapper endAt(String path) {
        return new QueryWrapperMock(elements,  numOfValueListener, numOfChildListener);
    }

    @Override
    public QueryWrapper limitToFirst(int num) {
        return new QueryWrapperMock(elements.subList(0, num - 1),  numOfValueListener, numOfChildListener);
    }

    @Override
    public QueryWrapper equalTo(Boolean b) {
        List<Leaf> newLeafs = new ArrayList<>();
        for(Leaf l: elements) {
            Match p = (Match) l.getData();
            if(p.isPrivateMatch() == b) {
                newLeafs.add(l);
            }
        }
        return new QueryWrapperMock(newLeafs,  numOfValueListener, numOfChildListener);
    }

    @Override
    public ValueEventListener addValueEventListener(final ValueEventListener listener) {
        ++numOfValueListener;
        new Thread() {
            public void run() {
                for(Leaf l: elements) {
                    Player p = null;
                    Match m = null;
                    DataSnapshot dSnap = mock(DataSnapshot.class);
                    Object obj = l.getData();
                    if(obj instanceof Player) {
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
                for (final Leaf l : elements) {

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
                                id = m.getMatchID().toString();
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