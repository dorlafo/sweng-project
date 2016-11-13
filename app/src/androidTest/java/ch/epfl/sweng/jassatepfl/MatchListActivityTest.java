package ch.epfl.sweng.jassatepfl;


import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.injections.InjectedBaseActivityTest;
import ch.epfl.sweng.jassatepfl.model.Match;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public final class MatchListActivityTest extends InjectedBaseActivityTest {

    public MatchListActivityTest() {
        super(MatchListActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    /* Need support of queries orderByChild and equalTo in mockito
    @Test
    public void testEmptyListDisplay() {
        Set<Match> emptyMatchSet = new HashSet<>();
        dbRefWrapMock.addMatches(emptyMatchSet);

        getActivity();

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

        getActivity();

        try {
        } catch (Exception e) {
            fail();
        }

        dbRefWrapMock.reset();
    }
    */

}
