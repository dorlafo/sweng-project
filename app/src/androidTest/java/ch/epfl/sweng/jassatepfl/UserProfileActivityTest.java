package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * UserProfileActivityTest
 */
@RunWith(AndroidJUnit4.class)
public final class UserProfileActivityTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<UserProfileActivity> activityRule =
            new ActivityTestRule<>(UserProfileActivity.class, false, false);

    @Override
    public void setUp() {
        super.setUp();
        dbRefWrapTest.reset();
    }

    /**
     * This test verifies that the assignment to the text view is correct
     */
    @Test
    public void testUserProfileActivityWithoutStats() {
        dbRefWrapTest.addPlayers(DummyDataTest.players());

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.profil_player)).check(matches(isDisplayed()));
        onView(withId(R.id.twQuoteNum)).check(matches(isDisplayed()));
        onView(withId(R.id.twMostPlayedWithName)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.twMostVariantName)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.twMatchPlayedNum)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.twMostWonWithName)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.twMatchWonNum)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    /**
     * This test verifies that the assignment to the text view is correct
     */
    @Test
    public void testUserProfileActivityWithStats() {
        dbRefWrapTest.addPlayers(DummyDataTest.players());
        dbRefWrapTest.addBobFakeStats();

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.profil_player)).check(matches(isDisplayed()));
        onView(withId(R.id.twQuote)).check(matches(isDisplayed()));
        onView(withId(R.id.twMostPlayedWith)).check(matches(isDisplayed()));
        onView(withId(R.id.twMostVariant)).check(matches(isDisplayed()));
        onView(withId(R.id.twMatchPlayed)).check(matches(isDisplayed()));
        onView(withId(R.id.twMostWonWith)).check(matches(isDisplayed()));
        onView(withId(R.id.twMatchWon)).check(matches(isDisplayed()));
    }

}
