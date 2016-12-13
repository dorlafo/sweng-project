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
import ch.epfl.sweng.jassatepfl.test_utils.database.local.MatchLeafTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.MatchStatusLeafTest;
import ch.epfl.sweng.jassatepfl.test_utils.mocks.DBRefWrapTest;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WaitingPlayersActivityTest extends InjectedBaseActivityTest {


    /*public WaitingPlayersActivityTest() {
        super(WaitingPlayersActivity.class);
    }*/
    @Rule
    public ActivityTestRule<WaitingPlayersActivity> activityRule =
            new ActivityTestRule<>(WaitingPlayersActivity.class, false, false);

    /*@Override
    public void setUp() throws Exception {
        super.setUp();
    }*/

    @Test
    public void testLeaveButton() {
        dbRefWrapTest.reset();
        fullMatchSetUp();
        //getActivity();
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.leave_match_button)).perform(click());
        onView(withText(R.string.main_empty_list)).check(matches(isDisplayed()));
        String matchID = DummyDataTest.fullMatchWithBob().getMatchID();
        Match m = ((MatchLeafTest) ((DBRefWrapTest) dbRefWrapTest.child(DatabaseUtils.DATABASE_MATCHES).child(matchID)).getCurrentNode()).getData();
        assertEquals(3, m.getPlayers().size());


        Map<String, Boolean> status = ((MatchStatusLeafTest) ((DBRefWrapTest) dbRefWrapTest.child(DatabaseUtils.DATABASE_PENDING_MATCHES).child(matchID)).getCurrentNode()).getData();
        assertEquals(3, status.size());
    }

    //TODO: re-enabled when removeListener will work on deleted references
    /*
    @Test
    public void testLeaveButtonWhenLastPlayerDeletesMatch() {
        dbRefWrapTest.reset();
        onePlayerMatchSetUp();
        getActivity();
        onView(withId(R.id.leave_match_button)).perform(click());
        onView(withId(R.id.twMyMatches)).check(matches(isDisplayed()));
        String matchID = DummyDataTest.onePlayerMatchWithBob().getMatchID();

        List<Match> matches = new ArrayList<>();
        Set<NodeTest> nodeMatches = ((NodeTest)dbRefWrapTest.child(DatabaseUtils.DATABASE_MATCHES)).getChildren();
        for(NodeTest matchLeaf: nodeMatches) {
            Match m = ((MatchLeafTest) matchLeaf).getData();
            matches.add(m);
        }

        assertEquals(0, matches.size());


        Map<String, Boolean> status = ((MatchStatusLeafTest)((DBRefWrapTest)dbRefWrapTest.child(DatabaseUtils.DATABASE_PENDING_MATCHES).child(matchID)).getCurrentNode()).getData();
        assertEquals(0, status.size());
    }
    */

    //TODO: re-enabled when childEventListener will work...
    /*
    @Test
    public void testUserIsReadyWorks() {
        dbRefWrapTest.reset();
        onePlayerMatchSetUp();
        getActivity();
        onView(withId(R.id.ready_button)).perform(click());
        String matchID = DummyDataTest.onePlayerMatchWithBob().getMatchID();

        Map<String, Boolean> status = ((MatchStatusLeafTest)((DBRefWrapTest)dbRefWrapTest.child(DatabaseUtils.DATABASE_PENDING_MATCHES).child(matchID)).getCurrentNode()).getData();
        assertEquals(1, status.size());
        assertTrue(status.get(DummyDataTest.bricoloBob.getID().toString()));

        onView(withId(R.id.ready_button)).check(matches(not(isEnabled())));
    }
    */

    @Test
    public void testSwitchToInvitePlayerActivity() {
        dbRefWrapTest.reset();
        onePlayerMatchSetUp();
        //getActivity();
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.invite_button)).perform(click());
        onView(withId(R.id.invite_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testYesCardsButton(){
        dbRefWrapTest.reset();
        fullMatchSetUp();
        //getActivity();
        onView(withText("Yes")).perform(click());
        onView(withId(R.id.cards_yes)).check(matches(isDisplayed()));
    }

    @Test
    public void testNoCardsButton(){
        dbRefWrapTest.reset();
        onePlayerMatchSetUp();
        //getActivity();
        onView(withText(R.string.dialog_have_cards)).perform(click());
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.cards_no)).check(matches(isDisplayed()));
        onView(withId(R.id.cards_yes)).check(matches(not(isDisplayed())));
    }


    private void onePlayerMatchSetUp() {
        Intent intent = new Intent();
        intent.putExtra("match_Id", DummyDataTest.onePlayerMatchWithBob().getMatchID());
        //setActivityIntent(intent);
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
        //setActivityIntent(intent);
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
