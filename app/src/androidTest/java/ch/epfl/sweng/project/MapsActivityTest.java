package ch.epfl.sweng.project;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public final class MapsActivityTest {

    @Rule
    public ActivityTestRule<MapsActivity> activityTestRule =
            new ActivityTestRule<>(MapsActivity.class);

    @Test
    public void testSwitchToListFromMap() {
        onView(withId(R.id.switch_to_list)).perform(click());
        onView(withId(android.R.id.list)).check(matches(isEnabled()));
    }
}
