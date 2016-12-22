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
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertMatchContainsNPlayers;
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertMatchesDBContainsNMatches;
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertPendingMatchContainsNPlayers;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class WaitingPlayersActivityTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<WaitingPlayersActivity> activityRule =
            new ActivityTestRule<>(WaitingPlayersActivity.class, false, false);

    @Override
    public void setUp() {
        super.setUp();
        dbRefWrapTest.reset();
    }

    @Test
    public void testLeaveButton() {
        fullMatchSetUp();
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.leave_match_button)).perform(click());
        onView(withText(R.string.main_empty_pending_list)).check(matches(isDisplayed()));
        String matchID = DummyDataTest.fullMatchWithBob().getMatchID();

        assertMatchContainsNPlayers(dbRefWrapTest, matchID, 3);

        assertPendingMatchContainsNPlayers(dbRefWrapTest, matchID, 3);
    }

    @Test
    public void testLeaveButtonWhenLastPlayerDeletesMatch() {
        onePlayerMatchSetUp();

        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.leave_match_button)).perform(click());
        onView(withText(R.string.main_empty_pending_list)).check(matches(isDisplayed()));
        String matchID = DummyDataTest.fullMatchWithBob().getMatchID();

        assertMatchesDBContainsNMatches(dbRefWrapTest, 0);
        assertPendingMatchContainsNPlayers(dbRefWrapTest, matchID, 0);
    }


    //TODO: re-enabled when childEventListener will work...
    /*
    @Test
    public void testUserIsReadyWorks() {
        onePlayerMatchSetUp();

        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.ready_button)).perform(click());
        String matchID = DummyDataTest.onePlayerMatchWithBob().obtainMatchID();

        assertPendingMatchContainsNPlayers(dbRefWrapTest, matchID, 1);
        assertTrue(status.get(DummyDataTest.bricoloBob.getID().toString()));

        onView(withId(R.id.ready_button)).check(matches(not(isEnabled())));
    }
    */

    @Test
    public void testSwitchToInvitePlayerActivity() {
        onePlayerMatchSetUp();
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.invite_button)).perform(click());
        onView(withId(R.id.invite_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testYesCardsButton(){
        fullMatchSetUp();
        onView(withText("Yes")).perform(click());
        onView(withId(R.id.cards_yes)).check(matches(isDisplayed()));
    }

    @Test
    public void testNoCardsButton(){
        onePlayerMatchSetUp();
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.cards_no)).check(matches(isDisplayed()));
        onView(withId(R.id.cards_yes)).check(matches(not(isDisplayed())));
    }


    private void onePlayerMatchSetUp() {
        Intent intent = new Intent();
        intent.putExtra("match_Id", DummyDataTest.onePlayerMatchWithBob().getMatchID());
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.onePlayerMatchWithBob());
        Map<String, Boolean> status = new HashMap<>();
        status.put(DummyDataTest.bricoloBob.getID().toString(), false);
        dbRefWrapTest.addPendingMatch(DummyDataTest.onePlayerMatchWithBob(), status);
        dbRefWrapTest.addMatches(matches);
        dbRefWrapTest.addPlayers(DummyDataTest.players());
        activityRule.launchActivity(intent);
    }

    private void fullMatchSetUp() {
        Intent intent = new Intent();
        intent.putExtra("match_Id", DummyDataTest.fullMatchWithBob().getMatchID());
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.fullMatchWithBob());
        Map<String, Boolean> status = new HashMap<>();
        status.put(DummyDataTest.bricoloBob.getID().toString(), false);
        status.put(DummyDataTest.marco.getID().toString(), false);
        status.put(DummyDataTest.dorian.getID().toString(), false);
        status.put(DummyDataTest.vincenzo.getID().toString(), false);
        dbRefWrapTest.addPendingMatch(DummyDataTest.fullMatchWithBob(), status);
        dbRefWrapTest.addMatches(matches);
        dbRefWrapTest.addPlayers(DummyDataTest.players());
        activityRule.launchActivity(intent);
    }
}
