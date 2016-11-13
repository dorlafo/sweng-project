package ch.epfl.sweng.jassatepfl;


import android.support.test.InstrumentationRegistry;
import android.support.test.filters.Suppress;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public final class MapsActivityTest extends
        ActivityInstrumentationTestCase2<MapsActivity> {

    public MapsActivityTest() {
        super(MapsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    //Ignoring this test for now because we need mock test to mock the login and access the activity
    //This will be resolved in Sprint #7
    @Suppress
    public void testSwitchToListFromMap() {
        onView(withId(R.id.switch_to_list)).perform(click());
        onView(withId(android.R.id.list)).check(matches(isEnabled()));
    }

}
