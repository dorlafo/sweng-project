package ch.epfl.sweng.project;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public final class MapsActivityTest extends ActivityInstrumentationTestCase2 {
    public MapsActivityTest() {
        super(MapsActivity.class);
    }

    public void testSwitchToListFromMap() {
        getActivity();
        onView(withId(R.id.switch_to_list)).perform(click());
        onView(withId(android.R.id.list)).check(matches(isEnabled()));
    }
}
