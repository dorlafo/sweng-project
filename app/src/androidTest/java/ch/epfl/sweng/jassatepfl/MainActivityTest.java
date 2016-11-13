package ch.epfl.sweng.jassatepfl;

import android.support.test.filters.Suppress;

import org.junit.Test;

import ch.epfl.sweng.jassatepfl.injections.InjectedBaseActivityTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public final class MainActivityTest extends InjectedBaseActivityTest {

    MainActivity act;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testCantShowProfileIfLoggedOff() {
        act = (MainActivity)  getActivity();
        onView(withId(R.id.login_button)).check(matches(withText(R.string.login_button_text)));
    }

    //Ignoring this test for now because we need mock test to mock the login and access the activity
    //This will be resolved in Sprint #7
    @Suppress
    public void testSwitchToMatchListActivity() {
        onView(withId(R.id.main_list_button)).perform(click());
        onView(withId(R.id.switch_to_map)).check(matches(isDisplayed()));
    }

    //Ignoring this test for now because we need mock test to mock the login and access the activity
    //This will be resolved in Sprint #7
    @Suppress
    public void testCreateMatchButtonSwitchesToCorrectActivity() {
        getActivity();
        onView(withId(R.id.create_match_button)).perform(click());
        onView(withId(R.id.create_title)).check(matches(isDisplayed()));
    }

    //Ignoring this test for now because we need mock test to mock the login and access the activity
    //This will be resolved in Sprint #7
    @Suppress
    public void testSwitchToMapsActivity() {
        onView(withId(R.id.main_map_button)).perform(click());
        onView(withId(R.id.switch_to_list)).check(matches(isDisplayed()));
    }

    //Ignoring this test for now because we need mock test to mock the login and access the activity
    //This will be resolved in Sprint #7
    @Suppress
    public void testCanShowProfile() {

        getActivity();
        onView(withId(R.id.profile_button)).perform(click());
        onView(withId(R.id.twProfileTitle)).check(matches(withText("Profile")));
        onView(withId(R.id.menu_button)).perform(click());
        onView(withId(R.id.profile_button)).check(matches(withText("Profile")));
    }
}
