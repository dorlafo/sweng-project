package ch.epfl.sweng.project.helpers.mocks.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ch.epfl.sweng.project.model.Player;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import ch.epfl.sweng.project.UserProfileActivity;
import ch.epfl.sweng.project.model.Rank;

public class UserProfileMDR extends MockedDatabaseReference {
    private ValueEventListener vEL;
    DataSnapshot dSnapshot;

    public UserProfileMDR() {
        super();
        vEL = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player p = new Player(new Player.PlayerID(211603), "Michel", "Jean", new Rank(1000));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        addBehaviors();
    }

    @Override
    public void addBehaviors() {
        when(mockedDRef.child("players")).thenReturn(mockedDRef);
        when(mockedDRef.child(anyString())).thenReturn(mockedDRef);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                asyncServiceMethod();
                return null;
            }
        }).when(mockedDRef.addValueEventListener(vEL));

    }

    public void asyncServiceMethod(){
        Runnable task = new Runnable() {

            @Override
            public void run() {
                try {
                    vEL.onDataChange(dSnapshot);
                } catch (Exception ex) {
                    //handle error which cannot be thrown back
                }
            }
        };
        new Thread(task, "ServiceThread").start();
    }
}
