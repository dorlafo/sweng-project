package ch.epfl.sweng.jassatepfl;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public final class DrawerTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, false);

    @Before
    public void setUp() {
        //super.setUp();
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.twoPlayersMatch());
        matches.add(DummyDataTest.onePlayerMatch());
        dbRefWrapTest.addMatches(matches);
    }

    @Test
    public void testDrawerOpens() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
    }

    @Test
    public void testBackClosesDrawer() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.drawer_layout)).perform(ViewActions.pressBack());
        //pressBack();
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
    }

    @Test
    public void testToolbarButtonOpensDrawer() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
    }

    @Test
    public void testCanNavigateToCreateActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_create));
        onView(withId(R.id.create_create_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testCanNavigateToMapsActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_maps));
        onView(withId(R.id.maps_menu_button)).check(matches(isDisplayed()));
        dbRefWrapTest.reset();
    }

    @Test
    public void testCanNavigateToMainActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_profile));
        onView(withId(R.id.profil_player)).check(matches(isDisplayed()));
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_main));
        onView(withText(R.string.main_empty_pending_list)).check(matches(isDisplayed()));
    }

    @Test
    public void testCanNavigateToListActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_list));
        onView(withId(R.id.list_nearby_matches)).check(matches(isDisplayed()));
    }


    @Test
    public void testCanNavigateToProfileActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_profile));
        onView(withId(R.id.profil_player)).check(matches(isDisplayed()));
    }

    @Test
    public void testCanNavigateToUserGuideActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_user_guide));
        onView(withId(R.id.user_guide_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testCanNavigateToRulesActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_rules));
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testCanNavigateToScoreBoard() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_score));
        onView(withId(R.id.game_playing_to)).check(matches(isDisplayed()));
    }

    @Test
    public void testLogout() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_logout));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

}
