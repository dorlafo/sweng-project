package ch.epfl.sweng.jassatepfl;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;

import javax.inject.Inject;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.test_utils.injectionsTest.DaggerFakeGraphTest;
import ch.epfl.sweng.jassatepfl.test_utils.injectionsTest.FakeGraphTest;
import ch.epfl.sweng.jassatepfl.test_utils.injectionsTest.FakeModulesTest;
import ch.epfl.sweng.jassatepfl.test_utils.mocks.DBRefWrapTest;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

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
public class InjectedBaseActivityTest {

    @Inject
    protected DBReferenceWrapper dbReferenceWrapper;

    @Inject
    protected FirebaseAuth fAuth;

    protected DBRefWrapTest dbRefWrapTest;

    @Before
    public void setUp() {
        App app = (App) getInstrumentation().getTargetContext().getApplicationContext();
        FakeGraphTest component = DaggerFakeGraphTest.builder().fakeModulesTest(new FakeModulesTest()).build();
        app.setGraph(component);
        component.inject(this);
        dbRefWrapTest = (DBRefWrapTest) dbReferenceWrapper;
    }

}
