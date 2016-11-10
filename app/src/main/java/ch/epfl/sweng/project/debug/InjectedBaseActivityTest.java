package ch.epfl.sweng.project.debug;

import android.test.ActivityInstrumentationTestCase2;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import ch.epfl.sweng.project.App;
import ch.epfl.sweng.project.database.helpers.DBReferenceWrapper;

public class InjectedBaseActivityTest extends ActivityInstrumentationTestCase2 {
    @Inject
    protected DBReferenceWrapper dbRefWrapped;

    @Inject
    FirebaseAuth fAuth;

    public InjectedBaseActivityTest(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        App app = (App) getInstrumentation().getTargetContext().getApplicationContext();
        app.setMockMode(true);
        app.graph().inject(this);
    }
    @Override
    protected void tearDown() throws Exception {
        App.getInstance().setMockMode(false);
    }
}
