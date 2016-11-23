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

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
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
