package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.view.View;

import org.hamcrest.Matcher;
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
        Match m = ((MatchLeafTest)((DBRefWrapTest)dbRefWrapTest.child(DatabaseUtils.DATABASE_MATCHES).child(matchID)).getCurrentNode()).getData();
        assertEquals(3, m.getPlayers().size());


        Map<String, Boolean> status = ((MatchStatusLeafTest)((DBRefWrapTest)dbRefWrapTest.child(DatabaseUtils.DATABASE_PENDING_MATCHES).child(matchID)).getCurrentNode()).getData();
        assertEquals(3, status.size());
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
