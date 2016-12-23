package ch.epfl.sweng.jassatepfl;

import android.app.Instrumentation;
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

import static android.app.Activity.RESULT_CANCELED;
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
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertMatchesDBContainsNMatches;
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertPendingMatchContainsNPlayers;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.amaury;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.jimmy;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class WaitingPlayersActivityTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<WaitingPlayersActivity> activityRule =
            new ActivityTestRule<>(WaitingPlayersActivity.class, false, false);

    @Test
    public void testLeaveButton() {
        //init();
        fullMatchSetUp();
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        //intending(hasComponent(MainActivity.class.getName()))
        //      .respondWith(new Instrumentation.ActivityResult(RESULT_OK, null));
        onView(withId(R.id.leave_match_button)).perform(click());
        onView(withText(R.string.main_empty_pending_list)).check(matches(isDisplayed()));
        //intended(hasComponent(MainActivity.class.getName()));

        String matchID = DummyDataTest.fullMatchWithBob().getMatchID();
        assertMatchContainsNPlayers(dbRefWrapTest, matchID, 3);
        assertPendingMatchContainsNPlayers(dbRefWrapTest, matchID, 3);
        //release();
    }

    @Test
    public void testLeaveButtonWhenLastPlayerDeletesMatch() {
        //init();
        onePlayerMatchSetUp();
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        //intending(hasComponent(MainActivity.class.getName()))
        //  .respondWith(new Instrumentation.ActivityResult(RESULT_OK, null));
        onView(withId(R.id.leave_match_button)).perform(click());
        onView(withText(R.string.main_empty_pending_list)).check(matches(isDisplayed()));
        //intended(hasComponent(MainActivity.class.getName()));

        String matchID = DummyDataTest.fullMatchWithBob().getMatchID();
        assertMatchesDBContainsNMatches(dbRefWrapTest, 0);
        assertPendingMatchContainsNPlayers(dbRefWrapTest, matchID, 0);
        //release();
    }

    @Test
    public void testInviteResultIsHandled() {
        init();
        onePlayerMatchSetUp();
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        Intent resultData = new Intent();
        resultData.putExtra("player0", amaury.getID().toString());
        resultData.putExtra("player1", jimmy.getID().toString());
        resultData.putExtra("players_added", 2);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(RESULT_CANCELED, resultData); // WTF?!

        intending(hasComponent(InvitePlayerToMatchActivity.class.getName())).respondWith(result);
        onView(withId(R.id.invite_button)).perform(click());
        intended(hasComponent(InvitePlayerToMatchActivity.class.getName()));

        release();
    }

    @Test
    public void testYesCardsButton() {
        fullMatchSetUp();
        onView(withText("Yes")).perform(click());
        onView(withId(R.id.cards_yes)).check(matches(isDisplayed()));
    }

    @Test
    public void testNoCardsButton() {
        onePlayerMatchSetUp();
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.cards_no)).check(matches(isDisplayed()));
        onView(withId(R.id.cards_yes)).check(matches(not(isDisplayed())));
    }

    private void onePlayerMatchSetUp() {
        dbRefWrapTest.reset();
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
        dbRefWrapTest.reset();
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
