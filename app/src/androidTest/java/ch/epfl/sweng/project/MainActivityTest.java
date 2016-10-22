package ch.epfl.sweng.project;

import android.test.ActivityInstrumentationTestCase2;
import android.support.test.InstrumentationRegistry;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import org.junit.Test;

public final class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
    }

    public void testCanShowProfile() {
        getActivity();
        onView(withId(R.id.profile_button)).perform(click());
        onView(withId(R.id.twProfileTitle)).check(matches(withText("Profile")));
        onView(withId(R.id.menu_button)).perform(click());
        onView(withId(R.id.profile_button)).check(matches(withText("Profile")));
    }


    @Test
    public void createMatchButtonSwitchesToCorrectActivity() {
        onView(withId(R.id.create_match_button)).perform(click());
        onView(withId(R.id.create_title)).check(matches(isDisplayed()));
    }
}
