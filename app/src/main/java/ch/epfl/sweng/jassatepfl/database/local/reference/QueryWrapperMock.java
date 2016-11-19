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

    public QueryWrapperMock(List<Leaf> elems) {
        super();
        elements = new ArrayList<>(elems);
    }

    @Override
    public QueryWrapper startAt(String path) {
        int i = 0;
        int listSize = elements.size();
        List<Leaf> elems = new ArrayList<>(elements);
        for(Leaf l: elems) {
            if(!l.getId().startsWith(path)) elems.remove(l);
        }
        return new QueryWrapperMock(elems);
    }

    @Override
    public QueryWrapper endAt(String path) {
        return new QueryWrapperMock(elements);
    }

    public QueryWrapper limitToFirst(int num) {
        return new QueryWrapperMock(elements.subList(0, num - 1));
    }

    public ValueEventListener addValueEventListener(ValueEventListener listener) {
        final ValueEventListener v = invocation.getArgument(0);
        DataSnapshot obj = mock(DataSnapshot.class);
        Player p = null;
        Match m = null;

        if (((Leaf) dbRefWrapMock.getCurrentNode()).getData() instanceof Player) {
            p = (Player) ((Leaf) dbRefWrapMock.getCurrentNode()).getData();
        } else if (((Leaf) dbRefWrapMock.getCurrentNode()).getData() instanceof Match) {
            m = (Match) ((Leaf) dbRefWrapMock.getCurrentNode()).getData();
        }

        when(obj.getValue(Player.class)).thenReturn(p);
        when(obj.getValue(Match.class)).thenReturn(m);

        v.onDataChange((DataSnapshot) obj);
        return null;

    }
}
