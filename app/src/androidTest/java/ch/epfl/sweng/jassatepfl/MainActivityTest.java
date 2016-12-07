package ch.epfl.sweng.jassatepfl;

import org.junit.Test;

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

public final class MainActivityTest extends InjectedBaseActivityTest {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

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

        getActivity();


        try {
            onData(anything()).inAdapterView(withId(R.id.my_pending_matches_list)).atPosition(0).perform(click());
            onView(withText(R.string.wait_button_text_ready)).check(matches(isDisplayed()));
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

/* Need support of queries orderByChild and equalTo in mockito
    @Test
    public void testEmptyListDisplay() {
        Set<Match> emptyMatchSet = new HashSet<>();
        dbRefWrapMock.addMatches(emptyMatchSet);

        try {
            onView(withText(R.string.list_empty_list)).check(matches(isDisplayed()));
        } catch (Exception e) {
            fail();
        }

        dbRefWrapMock.reset();
    }

    @Test
    public void testClickingOnAMatchBringsUpDialog() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.onePlayerMatch());
        matches.add(DummyData.twoPlayersMatch());
        dbRefWrapMock.addMatches(matches);

        try {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
            onView(withText(R.string.dialog_join_match)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_message)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_cancel)).check(matches(isDisplayed()));
        } catch (Exception e) {
            fail();
        }

        dbRefWrapMock.reset();
    }

    @Test
    public void testAddPlayerOnListMatchActivity() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.onePlayerMatch());
        dbRefWrapMock.addMatches(matches);
        assertMatchContainsNPlayers(dbRefWrapMock, "one_player", 1);

        try {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            assertMatchContainsNPlayers(dbRefWrapMock, "one_player", 2);
            assertMatchContainsPlayer(dbRefWrapMock, "one_player", new Player.PlayerID("696969"));
        } catch (Exception e) {
            fail();
        }
        dbRefWrapMock.reset();
    }

    @Test
    public void testDoNotAddWhenMatchFull() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.fullMatch());
        dbRefWrapMock.addMatches(matches);
        assertMatchContainsNPlayers(dbRefWrapMock, "full", 4);

        try {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            onView(withId(R.string.error_cannot_join)).check(matches(isDisplayed()));
            onView(withId(R.string.error_match_full)).check(matches(isDisplayed()));
        } catch (Exception e) {
            fail();
        }
        dbRefWrapMock.reset();
    }

    @Test
    public void testDoNotAddWhenAlreadyInMatch() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.onePlayerMatch());
        dbRefWrapMock.addMatches(matches);
        assertMatchContainsNPlayers(dbRefWrapMock, "one_player", 1);

        try {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            assertMatchContainsNPlayers(dbRefWrapMock, "one_player", 2);
            assertMatchContainsPlayer(dbRefWrapMock, "one_player", new Player.PlayerID("696969"));

            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            onView(withId(R.string.error_cannot_join)).check(matches(isDisplayed()));
            onView(withId(R.string.error_already_in_match)).check(matches(isDisplayed()));
        } catch (Exception e){
            fail();
        }
        dbRefWrapMock.reset();
    }*/

}
