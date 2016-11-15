package ch.epfl.sweng.jassatepfl;

import org.junit.Test;

import ch.epfl.sweng.jassatepfl.injections.InjectedBaseActivityTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public final class MainActivityTest extends InjectedBaseActivityTest {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    /* Need matchlistadapter
    @Test
    public void testSwitchToMatchListActivity() {
        onView(withId(R.id.main_list_button)).perform(click());
        onView(withId(R.id.list_menu_button)).check(matches(isDisplayed()));
    }
    */

    @Test
    public void testCreateMatchButtonSwitchesToCorrectActivity() {
        onView(withId(R.id.create_match_button)).perform(click());
        onView(withId(R.id.create_create_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testSwitchToMapsActivity() {
        onView(withId(R.id.main_map_button)).perform(click());
        onView(withId(R.id.maps_menu_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testCanShowProfile() {
        onView(withId(R.id.profile_button)).perform(click());
        onView(withId(R.id.twPlayerID)).check(matches(withText("Player id : 696969")));
    }

}
