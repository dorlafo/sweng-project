package ch.epfl.sweng.jassatepfl;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

public final class MapsActivityTest extends InjectedBaseActivityTest {

    public MapsActivityTest() {
        super(MapsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testDrawerOpens() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.twoPlayersMatch());
        matches.add(DummyDataTest.onePlayerMatch());
        dbRefWrapTest.addMatches(matches);

        getActivity();

        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        onView(withId(R.id.maps_menu_button)).perform(click());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        dbRefWrapTest.reset();
    }

    /*@Test PASSES ONLY LOCALY
    public void testMarkerDisplaysDialog() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.match_one_p_california());
        dbRefWrapTest.addMatches(matches);

        getActivity();

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Rolex"));

        try {
            Thread.sleep(3000);
            marker.click();
            Thread.sleep(3000);
            device.click(device.getDisplayWidth()/2, device.getDisplayHeight()/3);
            Thread.sleep(3000);
            onView(withText(R.string.dialog_join_match)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_message)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_cancel)).check(matches(isDisplayed()));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        dbRefWrapTest.reset();
    }*/

    /*@Test WAIT NEW WAITING PLAYER ACTIVITY
    public void testAddPlayerOnMatchActivity() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.onePlayerMatch());
        dbRefWrapTest.addMatches(matches);

        getActivity();

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Rolex"));
        assertMatchContainsNPlayers(dbRefWrapTest, "one_player", 1);
        try {
            marker.click();
            onData(withText("Rolex")).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            assertMatchContainsNPlayers(dbRefWrapTest, "one_player", 2);
            assertMatchContainsPlayer(dbRefWrapTest, "one_player", new Player.PlayerID("696969"));
        } catch(Exception e) {
            fail();
        }
        dbRefWrapTest.reset();
    }*/

   /* @Test PASSES LOCALY
    public void testDoNoAddWhenMatchFull() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.matchFullCalifornia());
        dbRefWrapTest.addMatches(matches);

        getActivity();

        assertMatchContainsNPlayers(dbRefWrapTest, "fullCalifornia", 4);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("CO"));
        try {
            Thread.sleep(3000);
            marker.click();
            Thread.sleep(3000);
            device.click(device.getDisplayWidth()/2, device.getDisplayHeight()/3);
            Thread.sleep(3000);
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            onView(withText(R.string.error_cannot_join)).check(matches(isDisplayed()));
            onView(withText(R.string.error_match_full)).check(matches(isDisplayed()));
        } catch(Exception e) {
            fail();
        }
        dbRefWrapTest.reset();
    }

    @Test
    public void testDoNotAddWhenAlreadyInMatch() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyDataTest.matchBobCalifornia());
        dbRefWrapTest.addMatches(matches);

        getActivity();

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("CO"));
        assertMatchContainsNPlayers(dbRefWrapTest, "bobCalifornia", 1);
        assertMatchContainsPlayer(dbRefWrapTest, "bobCalifornia", new Player.PlayerID("696969"));
        try {
            Thread.sleep(3000);
            marker.click();
            Thread.sleep(3000);
            device.click(device.getDisplayWidth()/2, device.getDisplayHeight()/3);
            Thread.sleep(3000);
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            onView(withText(R.string.error_cannot_join)).check(matches(isDisplayed()));
            onView(withText(R.string.error_already_in_match)).check(matches(isDisplayed()));
        } catch (Exception e) {
            fail();
        }
        dbRefWrapTest.reset();
    }*/

}
