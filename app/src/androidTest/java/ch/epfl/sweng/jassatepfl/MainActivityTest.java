package ch.epfl.sweng.jassatepfl;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.app.Activity.RESULT_CANCELED;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertMatchContainsNPlayers;
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertMatchContainsPlayer;
import static org.hamcrest.core.IsAnything.anything;

@RunWith(AndroidJUnit4.class)
public final class MainActivityTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void matchSetUp() {
        dbRefWrapTest.reset();
        Set<Match> matches = new HashSet<>();
        Match m = DummyDataTest.matchWithBob();
        matches.add(m);
        dbRefWrapTest.addMatches(matches);
        Map<String, Boolean> pendingMatches = new HashMap<>();
        pendingMatches.put(DummyDataTest.bricoloBob.getID().toString(), false);
        dbRefWrapTest.addPendingMatch(DummyDataTest.matchWithBob(), pendingMatches);
        assertMatchContainsNPlayers(dbRefWrapTest, "bob", 1);
        assertMatchContainsPlayer(dbRefWrapTest, "bob", new Player.PlayerID("696969"));
    }

    @Test
    public void testSwitchToWaitingPlayersActivityWhenClickOnAMatch() {
        init();
        activityRule.launchActivity(new Intent());

        Matcher<Intent> expectedIntent = hasComponent(WaitingPlayersActivity.class.getName());
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(RESULT_CANCELED, null));

        onData(anything()).inAdapterView(withId(R.id.my_pending_matches_list)).atPosition(0).perform(click());

        intended(expectedIntent);
        release();
    }

    @Test
    public void testMainWithIntent() {
        activityRule.launchActivity(new Intent().putExtra("notif", "matchexpired"));

        onView(withText(R.string.notification_match_expired)).check(matches(isDisplayed()));
    }

}
