package ch.epfl.sweng.jassatepfl;


import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

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

public final class MatchListActivityTest extends InjectedBaseActivityTest {

    public MatchListActivityTest() {
        super(MatchListActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    // Need support of queries orderByChild and equalTo in mockito
    @Test
    public void testEmptyListDisplay() {
        Set<Match> emptyMatchSet = new HashSet<>();
        dbRefWrapTest.addMatches(emptyMatchSet);

        getActivity();
        try {
            onView(withText(R.string.list_empty_list)).check(matches(isDisplayed()));
        } catch (Exception e) {
            fail();
        }

        dbRefWrapTest.reset();
    }

    @Test
    public void testClickingOnAMatchBringsUpDialog() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.onePlayerMatch());
        matches.add(DummyDataTest.twoPlayersMatch());
        dbRefWrapTest.addMatches(matches);

        getActivity();

        try {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
            onView(withText(R.string.dialog_join_match)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_message)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_cancel)).check(matches(isDisplayed()));
        } catch (Exception e) {
            fail();
        }

        dbRefWrapTest.reset();
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
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            assertMatchContainsNPlayers(dbRefWrapTest, "one_player", 2);
            assertMatchContainsPlayer(dbRefWrapTest, "one_player", new Player.PlayerID("696969"));
        } catch (Exception e) {
            fail();
        }
        dbRefWrapTest.reset();
    }*/

    @Test
    public void testDoNotAddWhenMatchFull() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.fullMatch());
        dbRefWrapTest.addMatches(matches);
        Map<String, Boolean> pendingMatches = new HashMap<>();
        dbRefWrapTest.addPendingMatch(DummyDataTest.fullMatch(), pendingMatches);
        assertMatchContainsNPlayers(dbRefWrapTest, "full", 4);

        getActivity();

        try {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            onView(withText(R.string.error_cannot_join)).check(matches(isDisplayed()));
            onView(withText(R.string.error_match_full)).check(matches(isDisplayed()));
        } catch (Exception e) {
            fail();
        }
        dbRefWrapTest.reset();
    }
}
