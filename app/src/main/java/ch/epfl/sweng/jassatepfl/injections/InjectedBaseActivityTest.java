package ch.epfl.sweng.jassatepfl.injections;

import android.test.ActivityInstrumentationTestCase2;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import ch.epfl.sweng.jassatepfl.App;
import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.database.local.reference.DBRefWrapMock;

/**
 * InjectedBaseActivityTest is the base class for the tests. It needs to remains in the main folder
 * because Dagger injects some fields here.
 * <p>
 * The user used for the test is :
 * id : 696969
 * Last name : LeBricoleur
 * First name : Bob
 * Rank : 1000
 */
@SuppressWarnings("deprecation")
public class InjectedBaseActivityTest extends ActivityInstrumentationTestCase2 {

    @Inject
    protected DBReferenceWrapper dbReferenceWrapper;

    @Inject
    protected FirebaseAuth fAuth;

    protected DBRefWrapMock dbRefWrapMock;

    public InjectedBaseActivityTest(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        App app = (App) getInstrumentation().getTargetContext().getApplicationContext();
        app.setMockMode(true);
        App.getInstance().graph().inject(this);
        dbRefWrapMock = (DBRefWrapMock) dbReferenceWrapper;
    }

    @Override
    protected void tearDown() throws Exception {
        App.getInstance().setMockMode(false);
    }

}
