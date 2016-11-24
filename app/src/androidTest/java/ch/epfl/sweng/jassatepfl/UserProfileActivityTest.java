package ch.epfl.sweng.jassatepfl;

import android.widget.TextView;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.model.Rank;

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
        playerSet.add(new Player(new Player.PlayerID("123456"), "Not Pass", "You Shall", new Rank(123)));
        dbRefWrapTest.addPlayers(playerSet);

        //Start the activity
        act = (UserProfileActivity) getActivity();

        //Write your assertions
        try {
            Thread.sleep(3000);
            Field playerIDField = act.getClass().getDeclaredField("mtwPlayerID");
            Field playerLastNameField = act.getClass().getDeclaredField("mtwLastName");
            Field playerFirstNameField = act.getClass().getDeclaredField("mtwFirstName");
            Field playerRankField = act.getClass().getDeclaredField("mtwPlayerRank");
            playerIDField.setAccessible(true);
            playerLastNameField.setAccessible(true);
            playerFirstNameField.setAccessible(true);
            playerRankField.setAccessible(true);
            TextView idView = (TextView) playerIDField.get(act);
            TextView lnView = (TextView) playerLastNameField.get(act);
            TextView fnView = (TextView) playerFirstNameField.get(act);
            TextView rankView = (TextView) playerRankField.get(act);


            assertEquals("Player id : 696969", idView.getText().toString());
            assertEquals("Last name : LeBricoleur", lnView.getText().toString());
            assertEquals("First name : Bob", fnView.getText().toString());
            assertEquals("Rank : 1000", rankView.getText().toString());
        } catch (Exception e) {
            fail();
        }

        //Reset the local database
        dbRefWrapTest.reset();
    }

}
