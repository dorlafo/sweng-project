package ch.epfl.sweng.jassatepfl.database.local.reference;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    public QueryWrapperMock(List<Leaf> elems, String childOrder) {
        super();
        elements = new ArrayList<>(elems);
        this.childOrder = childOrder;
    }

    @Override
    public QueryWrapper startAt(String path) {
        int i = 0;
        int listSize = elements.size();
        List<Leaf> elems = new ArrayList<>(elements);
        for(Leaf l: elems) {
            if(!l.getId().startsWith(path)) elems.remove(l);
        }
        return new QueryWrapperMock(elems, childOrder);
    }

    @Override
    public QueryWrapper endAt(String path) {
        return new QueryWrapperMock(elements, childOrder);
    }

    @Override
    public QueryWrapper limitToFirst(int num) {
        return new QueryWrapperMock(elements.subList(0, num - 1), childOrder);
    }

    @Override
    public QueryWrapper equalTo(Boolean b) {

    }

    @Override
    public ValueEventListener addValueEventListener(final ValueEventListener listener) {
        new Thread() {
            public void run() {
                Player p = null;
                Match m = null;
                DataSnapshot dSnap = mock(DataSnapshot.class);

                for(Leaf l: elements) {
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
}
