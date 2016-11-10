package ch.epfl.sweng.jassatepfl;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public final class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    @Test
    public void testCantShowProfileIfLoggedOff() {
        onView(withId(R.id.login_button)).check(matches(withText(R.string.login_button_text)));
    }

    @Test
    public void testSwitchToMatchListActivity() {
        onView(withId(R.id.main_list_button)).perform(click());
        onView(withId(R.id.switch_to_map)).check(matches(isDisplayed()));
    }

    @Test
    public void testCreateMatchButtonSwitchesToCorrectActivity() {
        onView(withId(R.id.create_match_button)).perform(click());
        onView(withId(R.id.create_title)).check(matches(isDisplayed()));
    }

    @Test
    public void testSwitchToMapsActivity() {
        onView(withId(R.id.main_map_button)).perform(click());
        onView(withId(R.id.switch_to_list)).check(matches(isDisplayed()));
    }

    /*
    * Test to redo with a mock login since it now required to be logged in to acces the main
    * activity
    *
    @Test
    public void testCanShowProfile() {

        getActivity();
        onView(withId(R.id.profile_button)).perform(click());
        onView(withId(R.id.twProfileTitle)).check(matches(withText("Profile")));
        onView(withId(R.id.menu_button)).perform(click());
        onView(withId(R.id.profile_button)).check(matches(withText("Profile")));
    }
    */

}
