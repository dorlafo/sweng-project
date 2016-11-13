package ch.epfl.sweng.jassatepfl;

import android.widget.TextView;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.injections.InjectedBaseActivityTest;
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
        dbRefWrapMock.addPlayers(playerSet);

        //Start the activity
        act = (UserProfileActivity) getActivity();

        //Write your assertions
        try {
            Field f = act.getClass().getDeclaredField("mtwPlayerID");
            f.setAccessible(true);
            TextView tView = (TextView) f.get(act);
            assertEquals("Player id : 696969", tView.getText().toString());
        } catch (Exception e) {
            fail();
        }

        //Reset the local database
        dbRefWrapMock.reset();
    }

}
