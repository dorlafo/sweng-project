package ch.epfl.sweng.project;


import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

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
    }

    @Test
    public void testSwitchToListFromMap() {
        getActivity();
        onView(withId(R.id.switch_to_list)).perform(click());
        onView(withId(android.R.id.list)).check(matches(isEnabled()));
    }

}
