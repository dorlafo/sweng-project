package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.MatchLeafTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.MatchStatusLeafTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.NodeTest;
import ch.epfl.sweng.jassatepfl.test_utils.database.local.TreeNodeTest;
import ch.epfl.sweng.jassatepfl.test_utils.mocks.DBRefWrapTest;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertMatchContainsNPlayers;
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertMatchesDBContainsNMatches;
import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertPendingMatchContainsNPlayers;
import static org.hamcrest.Matchers.not;

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
        onView(withId(R.id.twMyMatches)).check(matches(isDisplayed()));
        String matchID = DummyDataTest.fullMatchWithBob().getMatchID();

        assertMatchContainsNPlayers(dbRefWrapTest, matchID, 3);

        assertPendingMatchContainsNPlayers(dbRefWrapTest, matchID, 3);
    }

    @Test
    public void testLeaveButtonWhenLastPlayerDeletesMatch() {
        dbRefWrapTest.reset();
        onePlayerMatchSetUp();
        getActivity();
        onView(withId(R.id.leave_match_button)).perform(click());
        onView(withId(R.id.twMyMatches)).check(matches(isDisplayed()));
        String matchID = DummyDataTest.onePlayerMatchWithBob().getMatchID();

        assertMatchesDBContainsNMatches(dbRefWrapTest, 0);

        assertPendingMatchContainsNPlayers(dbRefWrapTest, matchID, 0);
    }

    //TODO: re-enabled when childEventListener will work...
    /*
    @Test
    public void testUserIsReadyWorks() {
        dbRefWrapTest.reset();
        onePlayerMatchSetUp();
        getActivity();
        onView(withId(R.id.ready_button)).perform(click());
        String matchID = DummyDataTest.onePlayerMatchWithBob().getMatchID();

        assertPendingMatchContainsNPlayers(dbRefWrapTest, matchID, 1);

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
