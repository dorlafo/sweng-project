package ch.epfl.sweng.jassatepfl;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.app.Activity.RESULT_CANCELED;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public final class DrawerTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, false);

    @Override
    public void setUp() {
        super.setUp();
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.twoPlayersMatch());
        matches.add(DummyDataTest.onePlayerMatch());
        dbRefWrapTest.addMatches(matches);
    }

    @Before
    public void intentSetUp() {
        init();
        intending(anyIntent())
                .respondWith(new Instrumentation.ActivityResult(RESULT_CANCELED, null));
    }

    @After
    public void intentRelease() {
        release();
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
        intended(hasComponent(CreateMatchActivity.class.getName()));
    }

    @Test
    public void testCanNavigateToMapsActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_maps));
        intended(hasComponent(MapsActivity.class.getName()));
        dbRefWrapTest.reset();
    }

    @Test
    public void testCanNavigateToMainActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_main));
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void testCanNavigateToListActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_list));
        intended(hasComponent(MatchListActivity.class.getName()));
    }


    @Test
    public void testCanNavigateToProfileActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_profile));
        intended(hasComponent(UserProfileActivity.class.getName()));
    }

    @Test
    public void testCanNavigateToUserGuideActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_user_guide));
        intended(hasComponent(UserGuideActivity.class.getName()));
    }

    @Test
    public void testCanNavigateToRulesActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_rules));
        intended(hasComponent(RulesActivity.class.getName()));
    }

    @Test
    public void testCanNavigateToScoreBoard() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_score));
        intended(hasComponent(GameActivity.class.getName()));
    }

    @Test
    public void testCanNavigateToStatsActivity() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_stats));
        intended(hasComponent(StatsActivity.class.getName()));
    }

    @Test
    public void testLogout() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_logout));
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void testExit() {
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_exit));
        intended(hasAction(Intent.ACTION_MAIN));
    }

}
