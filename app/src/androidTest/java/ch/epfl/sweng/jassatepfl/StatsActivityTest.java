package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsAnything.anything;

/**
 * @author Alexis Montavon
 *
 * Test class for StatsActivity
 */
@RunWith(AndroidJUnit4.class)
public class StatsActivityTest extends InjectedBaseActivityTest {

    /*public StatsActivityTest() {
        super(StatsActivity.class);
    }*/
    @Rule
    public ActivityTestRule<StatsActivity> activityRule =
            new ActivityTestRule<>(StatsActivity.class, false, false);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dbRefWrapTest.reset();
    }

    @Test
    public void testEmptyListDisplayInLeaderboard() {
        Set<Player> emptyPlayerSet = new HashSet<>();
        dbRefWrapTest.addPlayers(emptyPlayerSet);

        //getActivity();
        //activityRule.getActivity();
        activityRule.launchActivity(new Intent());

        onView(withText("Leaderboard")).perform(click());

        onView(withText(R.string.loading_leaderboard)).check(matches(isDisplayed()));
        /*
        try {
            onView(withText(R.string.loading_leaderboard)).check(matches(isDisplayed()));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }*/
    }

    @Test
    public void testLeaderBoardWithSomePlayerAtRightPosition(){
        Set<Player> playerSet = new HashSet<>();
        playerSet.add(DummyDataTest.bricoloBob);
        playerSet.add(DummyDataTest.amaury);
        playerSet.add(DummyDataTest.alexis);
        dbRefWrapTest.addPlayers(playerSet);

        //getActivity();
        //activityRule.getActivity();
        activityRule.launchActivity(new Intent());
        onView(withText("Leaderboard")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.leaderboard_list))
                .atPosition(0).check(matches(hasDescendant(withText("Bob LeBricoleur"))));
        onData(anything()).inAdapterView(withId(R.id.leaderboard_list))
                .atPosition(1).check(matches(hasDescendant(withText("Alexis Montavon"))));
        onData(anything()).inAdapterView(withId(R.id.leaderboard_list))
                .atPosition(2).check(matches(hasDescendant(withText("Amaury Combes"))));
        /*
        try {
            onData(anything()).inAdapterView(withId(R.id.leaderboard_list))
                    .atPosition(0).check(matches(hasDescendant(withText("Bob LeBricoleur"))));
            onData(anything()).inAdapterView(withId(R.id.leaderboard_list))
                    .atPosition(1).check(matches(hasDescendant(withText("Alexis Montavon"))));
            onData(anything()).inAdapterView(withId(R.id.leaderboard_list))
                    .atPosition(2).check(matches(hasDescendant(withText("Amaury Combes"))));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }*/
    }

    @Test
    public void testMoveBetweenFragments() {
        //getActivity();
        //activityRule.getActivity();
        activityRule.launchActivity(new Intent());
        onView(withText("Leaderboard")).perform(click());
        onView(withText("Evolution")).perform(click());
        onView(withText("Counters")).perform(click());
        /*
        try {
            onView(withText("Leaderboard")).perform(click());
            onView(withText("Evolution")).perform(click());
            onView(withText("Counters")).perform(click());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }*/
    }
}
