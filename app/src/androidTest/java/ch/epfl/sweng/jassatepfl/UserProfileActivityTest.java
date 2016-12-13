package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.bricoloBob;

/**
 * UserProfileActivityTest show an example of mock usage
 */
@RunWith(AndroidJUnit4.class)
public final class UserProfileActivityTest extends InjectedBaseActivityTest {

    /*public UserProfileActivityTest() {
        super(UserProfileActivity.class);
    }*/
    @Rule
    public ActivityTestRule<UserProfileActivity> activityRule =
            new ActivityTestRule<>(UserProfileActivity.class, false, false);

    /*@Override
    public void setUp() throws Exception {
        super.setUp();
    }*/

    /**
     * This test verifies that the assignment to the text view is correct
     */
    @Test
    public void testUserProfileActivity() {
        //Reset the local database
        dbRefWrapTest.reset();

        //Fill the database as you want with addPlayers and addMatches
        //Set<Player> playerSet = new HashSet<>();
        //playerSet.add(new Player(new Player.PlayerID("123456"), "Not Pass", "You Shall", 123));
        //dbRefWrapTest.addPlayers(playerSet);
        dbRefWrapTest.addPlayers(DummyDataTest.players());

        //Start the activity
        //getActivity();
        //activityRule.getActivity();
        activityRule.launchActivity(new Intent());
        //Write your assertions
        onView(withId(R.id.twPlayerID)).check(matches(withText("Player id : " + bricoloBob.getID().toString())));
        onView(withId(R.id.twLastName)).check(matches(withText("Last name : " + bricoloBob.getLastName())));
        onView(withId(R.id.twFirstName)).check(matches(withText("First name : " + bricoloBob.getFirstName())));
        onView(withId(R.id.twQuote)).check(matches(withText("Quote : " + bricoloBob.getQuote())));
    }

}
