package ch.epfl.sweng.jassatepfl;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Spinner;

import org.junit.Before;
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
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
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
 * Test class for RulesActivity
 *
 * @author Alexis Montavon
 */
@RunWith(AndroidJUnit4.class)
public class RulesActivityTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<RulesActivity> activityRule =
            new ActivityTestRule<>(RulesActivity.class, false);

    @Before
    public void setUp() {
        onView(allOf(withParent(withId(R.id.toolbar)), instanceOf(Spinner.class)))
                .perform(click());
    }

    @Test
    public void testSpinnerSelectionPiqueDouble() {
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(PIQUE_DOUBLE))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionObenAbe() {
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(OBEN_ABE))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionUndenUfe() {
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(UNDEN_UFE))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionSlalom() {
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(SLALOM))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionJassMarrant() {
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(JASS_MARANT))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionChibre() {
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(CHIBRE))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelectionChicane() {
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(CHICANE))).perform(click());
        onView(withId(R.id.rules_text)).check(matches(isDisplayed()));
    }

}
