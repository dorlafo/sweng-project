package ch.epfl.sweng.project;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public final class MatchListActivityTest {

    @Rule
    public ActivityTestRule<MatchListActivity> activityTestRule =
            new ActivityTestRule<>(MatchListActivity.class);

    @Test
    public void testSwitchToMapFromList() {
        activityTestRule.getActivity();
        onView(withId(R.id.switch_to_map)).perform(click());
        onView(withId(R.id.switch_to_list)).check(matches(isDisplayed()));
    }
}
