package ch.epfl.sweng.jassatepfl.test_utils.injectionsTest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.RootTest;
import ch.epfl.sweng.jassatepfl.test_utils.mocks.DBRefWrapTest;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author Amaury Combes
 */
@Module
public class FakeModulesTest {

    /**
     * A DatabaseReference provider
     *
     * @return returns a DatabaseReference that can be mocked or not
     */
    @Provides
    @Singleton
    public DBReferenceWrapper provideDBReference() {
            DBRefWrapTest dbRefWrapTest = new DBRefWrapTest(FirebaseDatabase.getInstance().getReference());
            fillDB(dbRefWrapTest);
            return dbRefWrapTest;
    }

    /**
     * A firebase authentification provider
     *
     * @return returns a firebase authentification that can be mocked or not
     */
    @Provides
    @Singleton
    public FirebaseAuth provideDBAuth() {
            FirebaseAuth fAuth = mock(FirebaseAuth.class);
            addMockedBehaviorAuth(fAuth);
            return fAuth;
    }

    private void fillDB(DBRefWrapTest dbRef) {
        RootTest root = (RootTest) dbRef.getCurrentNode();
        root.initialize();
        root.getChild(DatabaseUtils.DATABASE_PLAYERS)
                .addChild("696969")
                .setData(new Player(new Player.PlayerID("696969"), "LeBricoleur", "Bob", 1000));
    }

    private void addMockedBehaviorAuth(FirebaseAuth fAuth) {
        FirebaseUser fUser = mock(FirebaseUser.class);
        when(fUser.getDisplayName()).thenReturn("696969");
        when(fAuth.getCurrentUser()).thenReturn(fUser);
    }
}
