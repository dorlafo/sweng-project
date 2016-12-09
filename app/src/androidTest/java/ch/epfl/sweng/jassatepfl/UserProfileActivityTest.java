package ch.epfl.sweng.jassatepfl;

import android.support.test.espresso.matcher.ViewMatchers;
import android.widget.TextView;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
    }

    /**
     * This test verifies that the assignment to the text view is correct
     */
    @Test
    public void testUserProfileActivityWithoutStats() {
        dbRefWrapTest.reset();
        dbRefWrapTest.addPlayers(DummyDataTest.players());
        getActivity();

        onView(withId(R.id.profil_player)).check(matches(isDisplayed()));
        onView(withId(R.id.twQuote)).check(matches(isDisplayed()));
        onView(withId(R.id.twMostPlayedWith)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.twMostVariant)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.twMatchPlayed)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.twMostWonWith)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.twMatchWon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    /**
     * This test verifies that the assignment to the text view is correct
     */
    @Test
    public void testUserProfileActivityWithStats() {
        dbRefWrapTest.reset();
        dbRefWrapTest.addPlayers(DummyDataTest.players());
        dbRefWrapTest.addBobyFakeStats();
        getActivity();

        onView(withId(R.id.profil_player)).check(matches(isDisplayed()));
        onView(withId(R.id.twQuote)).check(matches(isDisplayed()));
        onView(withId(R.id.twMostPlayedWith)).check(matches(isDisplayed()));
        onView(withId(R.id.twMostVariant)).check(matches(isDisplayed()));
        onView(withId(R.id.twMatchPlayed)).check(matches(isDisplayed()));
        onView(withId(R.id.twMostWonWith)).check(matches(isDisplayed()));
        onView(withId(R.id.twMatchWon)).check(matches(isDisplayed()));
    }

}
