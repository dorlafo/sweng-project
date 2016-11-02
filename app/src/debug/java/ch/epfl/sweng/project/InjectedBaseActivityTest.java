package ch.epfl.sweng.project;

import android.test.ActivityInstrumentationTestCase2;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

public class InjectedBaseActivityTest extends ActivityInstrumentationTestCase2 {
    @Inject
    DatabaseReference dbRef;

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
