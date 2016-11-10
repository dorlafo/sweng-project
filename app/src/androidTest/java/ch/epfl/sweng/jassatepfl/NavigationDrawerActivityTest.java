package ch.epfl.sweng.jassatepfl;


import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public final class NavigationDrawerActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    public NavigationDrawerActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    @Test
    public void testBackClosesDrawer() {
        DrawerActions.open();
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        pressBack();
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
    }

}
