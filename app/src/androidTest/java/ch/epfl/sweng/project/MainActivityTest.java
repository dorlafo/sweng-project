package ch.epfl.sweng.project;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testCanShowProfile() {
        getActivity();
        onView(withId(R.id.profile_button)).perform(click());
        onView(withId(R.id.twRank)).check(matches(withText("Rank :")));
        onView(withId(R.id.menu_button)).perform(click());
        onView(withId(R.id.profile_button)).check(matches(withText("Profile")));
    }
}
