package ch.epfl.sweng.jassatepfl;

import android.content.Intent;

import org.junit.Test;

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

public class WaitingPlayersActivityTest extends InjectedBaseActivityTest {


    public WaitingPlayersActivityTest() {
        super(WaitingPlayersActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testLeaveButton() {
        dbRefWrapTest.reset();
        fullMatchSetUp();
        getActivity();
        onView(withId(R.id.leave_match_button)).perform(click());
        onView(withText(R.string.main_empty_pending_list)).check(matches(isDisplayed()));
        String matchID = DummyDataTest.fullMatchWithBob().getMatchID();
        Match m = ((MatchLeafTest) ((DBRefWrapTest) dbRefWrapTest.child(DatabaseUtils.DATABASE_MATCHES).child(matchID)).getCurrentNode()).getData();
        assertEquals(3, m.getPlayers().size());


        Map<String, Boolean> status = ((MatchStatusLeafTest) ((DBRefWrapTest) dbRefWrapTest.child(DatabaseUtils.DATABASE_PENDING_MATCHES).child(matchID)).getCurrentNode()).getData();
        assertEquals(3, status.size());
    }

    //TODO: re-enabled when removeListener will work on deleted references
    /*@Test
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
    }*/


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
        getActivity();
        onView(withId(R.id.invite_button)).perform(click());
        onView(withId(R.id.invite_button)).check(matches(isDisplayed()));
    }


    private void onePlayerMatchSetUp() {
        Intent intent = new Intent();
        intent.putExtra("match_Id", DummyDataTest.onePlayerMatchWithBob().getMatchID());
        setActivityIntent(intent);
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.onePlayerMatchWithBob());
        Map<String, Boolean> status = new HashMap<>();
        status.put(DummyDataTest.bricoloBob.getID().toString(), false);
        dbRefWrapTest.addPendingMatch(DummyDataTest.onePlayerMatchWithBob(), status);
        dbRefWrapTest.addMatches(matches);
        dbRefWrapTest.addPlayers(DummyDataTest.players());
    }

    private void fullMatchSetUp() {
        Intent intent = new Intent();
        intent.putExtra("match_Id", DummyDataTest.fullMatchWithBob().getMatchID());
        setActivityIntent(intent);
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
    }
}
