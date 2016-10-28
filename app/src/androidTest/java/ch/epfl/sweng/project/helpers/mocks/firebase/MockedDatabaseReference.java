package ch.epfl.sweng.project.helpers.mocks.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class MockedDatabaseReference {
    protected DatabaseReference mockedDRef;
    ValueEventListener v;

    public MockedDatabaseReference() {
        mockedDRef = mock(DatabaseReference.class);
    }

    public DatabaseReference provideMockedDatabaseReference() {
        return mockedDRef;
    }

    public abstract void addBehaviors();
}
