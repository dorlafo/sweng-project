package ch.epfl.sweng.project;


import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public final class MatchListActivityTest extends
        ActivityInstrumentationTestCase2<MatchListActivity> {

    public MatchListActivityTest() {
        super(MatchListActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testSwitchToMapFromList() {
        getActivity();
        onView(withId(R.id.switch_to_map)).perform(click());
        onView(withId(R.id.switch_to_list)).check(matches(isDisplayed()));
    }

}
