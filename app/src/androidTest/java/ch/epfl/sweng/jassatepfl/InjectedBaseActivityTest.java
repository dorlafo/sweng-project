package ch.epfl.sweng.jassatepfl;

import android.test.ActivityInstrumentationTestCase2;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import ch.epfl.sweng.jassatepfl.App;
import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;

public class InjectedBaseActivityTest extends ActivityInstrumentationTestCase2 {

    public InjectedBaseActivityTest(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        App app = (App) getInstrumentation().getTargetContext().getApplicationContext();
        app.setMockMode(true);
    }
    @Override
    protected void tearDown() throws Exception {
        App.getInstance().setMockMode(false);
    }
}
