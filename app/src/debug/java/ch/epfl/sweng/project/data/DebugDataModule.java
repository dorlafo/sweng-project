package ch.epfl.sweng.project.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Random;

import javax.inject.Singleton;

import ch.epfl.sweng.project.database.tools.DBRefWrapMock;
import ch.epfl.sweng.project.database.tools.DBReferenceWrapper;
import ch.epfl.sweng.project.database.tools.Leaf;
import ch.epfl.sweng.project.database.tools.Node;
import dagger.Module;
import dagger.Provides;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * DebugDataModule is module (i.e. a class that defines a set of providers which are the method
 * annotated with @Provides). The providers can provide their objects in normal mod or mocked mod.
 */
@Module
public final class DebugDataModule {
    private final boolean mockMode;

    /**
     * Constructor of the DebugDataModule class
     * @param provideMocks let us enable the mocking of our objects returned by the providers
     */
    public DebugDataModule(boolean provideMocks) {
        this.mockMode = provideMocks;
    }

    /**
     * A DatabaseReference provider
     * @return returns a DatabaseReference that can be mocked or not
     */
    @Provides @Singleton
    public DBReferenceWrapper provideDBReference() {
        if(mockMode) {
            DBRefWrapMock dbRefWrapMock = mock(DBRefWrapMock.class);
            addMockedBehavior(dbRefWrapMock);
            return dbRefWrapMock;
        } else {
            return new DBReferenceWrapper(FirebaseDatabase.getInstance().getReference());
        }
    }

    /**
     * A firebase authentification provider
     * @return returns a firebase authentification that can be mocked or not
     */
    @Provides @Singleton
    public FirebaseAuth provideDBAuth() {
        if(mockMode) {
            return mock(FirebaseAuth.class);
        } else {
            return FirebaseAuth.getInstance();
        }
    }

    private void addMockedBehavior(DBRefWrapMock dbRefWrapMock) {
        addMockedChildMethod(dbRefWrapMock);
        addMockedPushdMethod(dbRefWrapMock);
        addMockedGetKeyMethod(dbRefWrapMock);
        addMockedSetValueMethod(dbRefWrapMock);
        addMockedAddListenerForSingleValueEventMethod(dbRefWrapMock);
        addMockedAddChildEventListener(dbRefWrapMock);
    }

    private void addMockedChildMethod(final DBRefWrapMock dbRefWrapMock) {
        when(dbRefWrapMock.child(anyString())).thenAnswer(new Answer<DBReferenceWrapper>() {
            @Override
            public DBReferenceWrapper answer(InvocationOnMock invocation) throws Throwable {
                String arg = invocation.getArgument(0);
                return new DBRefWrapMock(dbRefWrapMock.getCurrentNode().getChild(arg));
            }
        });
    }

    private void addMockedPushdMethod(final DBRefWrapMock dbRefWrapMock) {
        doAnswer(new Answer<DBReferenceWrapper>() {
            @Override
            public DBReferenceWrapper answer(InvocationOnMock invocation) throws Throwable {
                Node currentNode = dbRefWrapMock.getCurrentNode();
                currentNode.addAutoGeneratedChild();
                return new DBRefWrapMock(currentNode);
            }
        }).when(dbRefWrapMock.push());
    }

    private void addMockedGetKeyMethod(DBRefWrapMock dbRefWrapMock) {
        when(dbRefWrapMock.getKey()).thenReturn(dbRefWrapMock.getCurrentNode().getId());
    }

    private void addMockedSetValueMethod(final DBRefWrapMock dbRefWrapMock) {
        doAnswer(new Answer<DBReferenceWrapper>() {
            @Override
            public DBReferenceWrapper answer(InvocationOnMock invocation) throws Throwable {
                Leaf currentNode = (Leaf) dbRefWrapMock.getCurrentNode();
                currentNode.setData(invocation.getArgument(0));
                return new DBRefWrapMock(currentNode);
            }

        }).when(dbRefWrapMock.setValue(anyObject()));
    }

    private void addMockedAddListenerForSingleValueEventMethod(DBRefWrapMock dbRefWrapMock) {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener v = invocation.getArgument(0);
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(30 + (new Random().nextInt(31)));
                            //TODO add the value to the local database (+ mock DataSnapshot)
                        } catch (Exception ex) {
                            //handle error which cannot be thrown back
                        }
                    }
                };
                new Thread(task, "ServiceThread").start();
                return null;
            }
        }).when(dbRefWrapMock).addListenerForSingleValueEvent(any(ValueEventListener.class));
    }

    private void addMockedAddChildEventListener(DBRefWrapMock dbRefWrapMock) {

    }
}
