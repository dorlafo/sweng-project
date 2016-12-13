package ch.epfl.sweng.jassatepfl;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertMatchContainsNPlayers;
import static org.hamcrest.core.IsAnything.anything;

@RunWith(AndroidJUnit4.class)
public final class MatchListActivityTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<MatchListActivity> activityRule =
            new ActivityTestRule<>(MatchListActivity.class, false, false);

    @Before
    public void setUp() {
        super.setUp();
        dbRefWrapTest.reset();
    }

    @Test
    public void testEmptyListDisplay() {
        Set<Match> emptyMatchSet = new HashSet<>();
        dbRefWrapTest.addMatches(emptyMatchSet);

        activityRule.launchActivity(new Intent());
        onView(withText(R.string.list_empty_list)).check(matches(isDisplayed()));
    }

    @Test
    public void testClickingOnAMatchBringsUpDialog() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.onePlayerMatch());
        matches.add(DummyDataTest.twoPlayersMatch());
        dbRefWrapTest.addMatches(matches);

        activityRule.launchActivity(new Intent());
        onData(anything()).inAdapterView(withId(R.id.list_nearby_matches)).atPosition(0).perform(click());
        onView(withText(R.string.dialog_join_match)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_join_message)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_cancel)).check(matches(isDisplayed()));
    }

    /*@Test WAITING NEW WAITING PLAYER ACTIVITY
    public void testAddPlayerOnListMatchActivity() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.onePlayerMatch());
        dbRefWrapTest.addMatches(matches);
        dbRefWrapTest.addPendingMatch(DummyDataTest.onePlayerMatch(), Arrays.asList(false, false, false, false));
        assertMatchContainsNPlayers(dbRefWrapTest, "one_player", 1);

        getActivity();

        try {
            onData(anything()).inAdapterView(withId(R.id.list_nearby_matches)).atPosition(0).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            assertMatchContainsNPlayers(dbRefWrapTest, "one_player", 2);
            assertMatchContainsPlayer(dbRefWrapTest, "one_player", new Player.PlayerID("696969"));
        } catch (Exception e) {
            fail();
        }
    }*/

    @Test
    public void testDoNotAddWhenMatchFull() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.fullMatch());
        dbRefWrapTest.addMatches(matches);
        Map<String, Boolean> pendingMatches = new HashMap<>();
        dbRefWrapTest.addPendingMatch(DummyDataTest.fullMatch(), pendingMatches);
        dbRefWrapTest.addPlayers(DummyDataTest.players());
        assertMatchContainsNPlayers(dbRefWrapTest, "full", 4);

        activityRule.launchActivity(new Intent());
        onData(anything()).inAdapterView(withId(R.id.list_nearby_matches)).atPosition(0).perform(click());
        onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_join_confirmation)).perform(click());
        onView(withText(R.string.error_cannot_join)).check(matches(isDisplayed()));
        onView(withText(R.string.error_match_full)).check(matches(isDisplayed()));
    }
}
