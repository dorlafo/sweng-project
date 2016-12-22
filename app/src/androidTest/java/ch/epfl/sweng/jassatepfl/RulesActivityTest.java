package ch.epfl.sweng.jassatepfl;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.jassatepfl.model.Match;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.jassatepfl.model.Match.GameVariant.CHIBRE;
import static ch.epfl.sweng.jassatepfl.model.Match.GameVariant.CHICANE;
import static ch.epfl.sweng.jassatepfl.model.Match.GameVariant.JASS_MARANT;
import static ch.epfl.sweng.jassatepfl.model.Match.GameVariant.OBEN_ABE;
import static ch.epfl.sweng.jassatepfl.model.Match.GameVariant.PIQUE_DOUBLE;
import static ch.epfl.sweng.jassatepfl.model.Match.GameVariant.SLALOM;
import static ch.epfl.sweng.jassatepfl.model.Match.GameVariant.UNDEN_UFE;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * @author Alexis Montavon
 *
 * Test class for RulesActivity
 */
@RunWith(AndroidJUnit4.class)
public class RulesActivityTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<RulesActivity> activityRule =
            new ActivityTestRule<>(RulesActivity.class, false);

    @Test
    public void testDisplay() {
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionPiqueDouble() {
        onView(withId(R.id.rules_spinner)).perform(click());
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(PIQUE_DOUBLE))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionObenAbe() {
        onView(withId(R.id.rules_spinner)).perform(click());
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(OBEN_ABE))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionUndenUfe() {
        onView(withId(R.id.rules_spinner)).perform(click());
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(UNDEN_UFE))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionSlalom() {
        onView(withId(R.id.rules_spinner)).perform(click());
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(SLALOM))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionJassMarrant() {
        onView(withId(R.id.rules_spinner)).perform(click());
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(JASS_MARANT))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionChibre() {
        onView(withId(R.id.rules_spinner)).perform(click());
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(CHIBRE))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionChicane() {
        onView(withId(R.id.rules_spinner)).perform(click());
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(CHICANE))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }
}
