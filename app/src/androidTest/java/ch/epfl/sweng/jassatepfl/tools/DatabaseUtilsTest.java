/*package ch.epfl.sweng.jassatepfl.tools;

import android.app.Activity;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.MainActivity;
import ch.epfl.sweng.jassatepfl.injections.InjectedBaseActivityTest;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.test_utils.DummyData;

import static ch.epfl.sweng.jassatepfl.test_utils.DBTestUtils.assertMatchContainsNPlayers;

/**
 * @author Alexis Montavon
 *
 * Test class for DatabaseUtils
 */
/*public class DatabaseUtilsTest extends InjectedBaseActivityTest {
    private Activity act;
    public DatabaseUtilsTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testAddPlayerWithMethod() {
        Set<Match> matches = new HashSet<>();
        Match matchOne = DummyDataTest.onePlayerMatch();
        matches.add(matchOne);
        dbRefWrapMock.addMatches(matches);

        act = getActivity();
        assertEquals(fAuth.getCurrentUser().getDisplayName(), "696969");

        assertMatchContainsNPlayers(dbRefWrapMock, "one_player", 1);
        DatabaseUtils.addPlayerToMatch(act, dbRefWrapMock, "one_player", "696969", matchOne);
        assertMatchContainsNPlayers(dbRefWrapMock, "one_player", 2);
    }
}*/
