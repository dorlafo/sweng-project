package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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

    @Test
    public void testSwitchToWaitingPlayersActivityWhenClickOnAMatch() {
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

        activityRule.launchActivity(new Intent());

        onData(anything()).inAdapterView(withId(R.id.my_pending_matches_list)).atPosition(0).perform(click());
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.wait_button_text_ready)).check(matches(isDisplayed()));
    }

    @Test
    public void testFinishedMatchesAreShown() {
        dbRefWrapTest.reset();
        /*
        Match match = DummyDataTest.fullMatchWithBob();
        MatchStats mS = new MatchStats(match);
        mS.setWinnerIndex(0);
        Set<MatchStats> matchStatsSet = new HashSet<>();
        matchStatsSet.add(mS);
        dbRefWrapTest.addMatchStatsArchive(matchStatsSet);
        */
        activityRule.launchActivity(new Intent());

        onView(withText(R.id.my_finished_matches_list)).perform(click());
        //onData(anything()).inAdapterView(withId(R.id.my_finished_matches_list)).atPosition(0).perform(click());
    }
    @Test
    public void testMainWithIntent() {
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

        activityRule.launchActivity(new Intent().putExtra("notif", "matchexpired"));

        onView(withText(R.string.notification_match_expired)).check(matches(isDisplayed()));
    }

}
