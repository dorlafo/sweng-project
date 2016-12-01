package ch.epfl.sweng.jassatepfl.injections;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.database.local.Root;
import ch.epfl.sweng.jassatepfl.database.local.reference.DBRefWrapMock;
import ch.epfl.sweng.jassatepfl.model.Player;
import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * DebugDataModule is module (i.e. a class that defines a set of providers which are the method
 * annotated with @Provides). The providers can provide their objects in normal mod or mocked mod.
 *
 * @author Amaury Combes
 */
@Module
public final class DebugDataModule {

    private final boolean mockMode;

    /**
     * Constructor of the DebugDataModule class
     *
     * @param provideMocks let us enable the mocking of our objects returned by the providers
     */
    public DebugDataModule(boolean provideMocks) {
        this.mockMode = provideMocks;
    }

    /**
     * A DatabaseReference provider
     *
     * @return returns a DatabaseReference that can be mocked or not
     */
    @Provides
    @Singleton
    public DBReferenceWrapper provideDBReference() {
        if (mockMode) {
            DBRefWrapMock dbRefWrapMock = new DBRefWrapMock(FirebaseDatabase.getInstance().getReference());
            fillDB(dbRefWrapMock);
            return dbRefWrapMock;
        } else {
            return new DBReferenceWrapper(FirebaseDatabase.getInstance().getReference());
        }
    }

    /**
     * A firebase authentification provider
     *
     * @return returns a firebase authentification that can be mocked or not
     */
    @Provides
    @Singleton
    public FirebaseAuth provideDBAuth() {
        if (mockMode) {
            FirebaseAuth fAuth = mock(FirebaseAuth.class);
            addMockedBehaviorAuth(fAuth);
            return fAuth;
        } else {
            return FirebaseAuth.getInstance();
        }
    }

    private void fillDB(DBRefWrapMock dbRef) {
        Root root = (Root) dbRef.getCurrentNode();
        root.initialize();
        root.getChild("players")
                .addChild("696969")
                .setData(new Player(new Player.PlayerID("696969"), "LeBricoleur", "Bob", 1000));
    }

    private void addMockedBehaviorAuth(FirebaseAuth fAuth) {
        FirebaseUser fUser = mock(FirebaseUser.class);
        when(fUser.getDisplayName()).thenReturn("696969");
        when(fAuth.getCurrentUser()).thenReturn(fUser);
    }
}
