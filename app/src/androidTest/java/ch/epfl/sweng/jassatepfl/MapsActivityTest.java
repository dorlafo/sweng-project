package ch.epfl.sweng.jassatepfl;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.injections.InjectedBaseActivityTest;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.test_utils.DummyData;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public final class MapsActivityTest extends InjectedBaseActivityTest {

    public MapsActivityTest() {
        super(MapsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    @Test
    public void testDrawerOpens() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        onView(withId(R.id.maps_menu_button)).perform(click());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
    }

    /* Not working for now
    @Test
    public void testMarkerDisplaysDialog() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.twoPlayersMatch());
        matches.add(DummyData.onePlayerMatch());
        dbRefWrapMock.addMatches(matches);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Rolex"));
        try {
            marker.click();
        } catch (UiObjectNotFoundException e) {
            fail();
        }
        dbRefWrapMock.reset();
    }
    */

}
