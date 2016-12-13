package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.bricoloBob;

/**
 * UserProfileActivityTest show an example of mock usage
 */
@RunWith(AndroidJUnit4.class)
public final class UserProfileActivityTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<UserProfileActivity> activityRule =
            new ActivityTestRule<>(UserProfileActivity.class, false, false);

    @Before
    public void setUp() {
        super.setUp();
        dbRefWrapTest.reset();
    }

    /**
     * This test verifies that the assignment to the text view is correct
     */
    @Test
    public void testUserProfileActivity() {
        dbRefWrapTest.addPlayers(DummyDataTest.players());

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.twPlayerID)).check(matches(withText("Player id : " + bricoloBob.getID().toString())));
        onView(withId(R.id.twLastName)).check(matches(withText("Last name : " + bricoloBob.getLastName())));
        onView(withId(R.id.twFirstName)).check(matches(withText("First name : " + bricoloBob.getFirstName())));
        onView(withId(R.id.twQuote)).check(matches(withText("Quote : " + bricoloBob.getQuote())));
    }

}
