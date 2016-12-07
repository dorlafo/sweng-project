package ch.epfl.sweng.jassatepfl;

import android.widget.TextView;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Player;

/**
 * UserProfileActivityTest show an example of mock usage
 */
public final class UserProfileActivityTest extends InjectedBaseActivityTest {

    UserProfileActivity act;

    public UserProfileActivityTest() {
        super(UserProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * This test verifies that the assignment to the text view is correct
     */
    @Test
    public void testUserProfileActivity() {
        //Fill the database as you want with addPlayers and addMatches
        Set<Player> playerSet = new HashSet<Player>();
        playerSet.add(new Player(new Player.PlayerID("123456"), "Not Pass", "You Shall", 123));
        dbRefWrapTest.addPlayers(playerSet);

        //Start the activity
        act = (UserProfileActivity) getActivity();

        //Write your assertions
        try {
            Thread.sleep(3000);
            Field playerField = act.getClass().getDeclaredField("mtwPlayer");
            Field playerQuoteField = act.getClass().getDeclaredField("mtwPlayerQuote");
            playerField.setAccessible(true);
            playerQuoteField.setAccessible(true);
            TextView playerView = (TextView) playerField.get(act);
            TextView quoteView = (TextView) playerQuoteField.get(act);

            assertEquals("Bob LeBricoleur", playerView.getText().toString());
            assertEquals("Quote : 1000", quoteView.getText().toString());
        } catch (Exception e) {
            fail();
        }

        //Reset the local database
        dbRefWrapTest.reset();
    }

}
