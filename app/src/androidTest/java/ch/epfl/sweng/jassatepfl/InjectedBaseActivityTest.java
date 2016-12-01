package ch.epfl.sweng.jassatepfl;

import android.test.ActivityInstrumentationTestCase2;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;
import ch.epfl.sweng.jassatepfl.test_utils.injectionsTest.DaggerFakeGraphTest;
import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.test_utils.injectionsTest.FakeGraphTest;
import ch.epfl.sweng.jassatepfl.test_utils.injectionsTest.FakeModulesTest;
import ch.epfl.sweng.jassatepfl.test_utils.mocks.DBRefWrapTest;

/**
 * InjectedBaseActivityTest is the base class for the tests. It needs to remains in the main folder
 * because Dagger injects some fields here.
 * <p>
 * The user used for the test is :
 * id : 696969
 * Last name : LeBricoleur
 * First name : Bob
 * Quote : 1000
 */
@SuppressWarnings("deprecation")
public class InjectedBaseActivityTest extends ActivityInstrumentationTestCase2 {

    @Inject
    protected DBReferenceWrapper dbReferenceWrapper;

    @Inject
    protected FirebaseAuth fAuth;

    protected DBRefWrapTest dbRefWrapTest;

    public InjectedBaseActivityTest(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        App app = (App) getInstrumentation().getTargetContext().getApplicationContext();
        FakeGraphTest component = DaggerFakeGraphTest.builder().fakeModulesTest(new FakeModulesTest()).build();
        app.setGraph(component);
        component.inject(this);
        //App.getInstance().graph().inject(this);
        dbRefWrapTest = (DBRefWrapTest) dbReferenceWrapper;
    }

    @Override
    protected void tearDown() throws Exception {

    }
}
