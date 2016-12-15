package ch.epfl.sweng.jassatepfl;


import org.junit.Test;

import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.bricoloBob;

/**
 * UserProfileActivityTest show an example of mock usage
 */
public final class UserProfileActivityTest extends InjectedBaseActivityTest {


    public UserProfileActivityTest() {
        super(UserProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dbRefWrapTest.reset();
    }

    /**
     * This test verifies that the assignment to the text view is correct
     */
    @Test
    public void testUserProfileActivity() {
        dbRefWrapTest.addPlayers(DummyDataTest.players());

        getActivity();

        onView(withId(R.id.twPlayerID)).check(matches(withText("Player id : " + bricoloBob.getID().toString())));
        onView(withId(R.id.twLastName)).check(matches(withText("Last name : " + bricoloBob.getLastName())));
        onView(withId(R.id.twFirstName)).check(matches(withText("First name : " + bricoloBob.getFirstName())));
        onView(withId(R.id.twQuote)).check(matches(withText("Quote : " + bricoloBob.getQuote())));

    }

}
