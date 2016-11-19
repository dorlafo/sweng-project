package ch.epfl.sweng.jassatepfl;

import ch.epfl.sweng.jassatepfl.injections.InjectedBaseActivityTest;

public final class MapsActivityTest extends InjectedBaseActivityTest {

    public MapsActivityTest() {
        super(MapsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    /* Not working for now
    @Test
    public void testDrawerOpens() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.twoPlayersMatch());
        matches.add(DummyData.onePlayerMatch());
        dbRefWrapMock.addMatches(matches);
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        onView(withId(R.id.maps_menu_button)).perform(click());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        dbRefWrapMock.reset();
    }


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
            onData(withText("Rolex")).perform(click());
            onView(withText(R.string.dialog_join_match)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_message)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_cancel)).check(matches(isDisplayed()));
        } catch (Exception e) {
            fail();
        }
        dbRefWrapMock.reset();
    }

    @Test
    public void testAddPlayerOnMatchActivity() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.onePlayerMatch());
        dbRefWrapMock.addMatches(matches);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Rolex"));
        assertMatchContainsNPlayers(dbRefWrapMock, "one_player", 1);
        try {
            marker.click();
            onData(withText("Rolex")).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            assertMatchContainsNPlayers(dbRefWrapMock, "one_player", 2);
            assertMatchContainsPlayer(dbRefWrapMock, "one_player", new Player.PlayerID("696969"));
        } catch(Exception e) {
            fail();
        }
        dbRefWrapMock.reset();
    }

    @Test
    public void testDoNoAddWhenMatchFull() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.fullMatch());
        dbRefWrapMock.addMatches(matches);
        assertMatchContainsNPlayers(dbRefWrapMock, "full", 4);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("CO"));
        try {
            marker.click();
            onData(withText("CO")).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            onView(withId(R.string.error_cannot_join)).check(matches(isDisplayed()));
            onView(withId(R.string.error_match_full)).check(matches(isDisplayed()));
        } catch(Exception e) {
            fail();
        }
        dbRefWrapMock.reset();
    }

    @Test
    public void testDoNotAddWhenAlreadyInMatch() {
        Set<Match> matches = new HashSet<>();
        matches.add(DummyData.onePlayerMatch());
        dbRefWrapMock.addMatches(matches);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Rolex"));
        assertMatchContainsNPlayers(dbRefWrapMock, "one_player", 1);
        try {
            marker.click();
            onData(withText("Rolex")).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            assertMatchContainsNPlayers(dbRefWrapMock, "one_player", 2);
            assertMatchContainsPlayer(dbRefWrapMock, "one_player", new Player.PlayerID("696969"));

            marker.click();
            onData(withText("Rolex")).perform(click());
            onView(withText(R.string.dialog_join_confirmation)).check(matches(isDisplayed()));
            onView(withText(R.string.dialog_join_confirmation)).perform(click());
            onView(withId(R.string.error_cannot_join)).check(matches(isDisplayed()));
            onView(withId(R.string.error_already_in_match)).check(matches(isDisplayed()));
        } catch (Exception e) {
            fail();
        }
        dbRefWrapMock.reset();
    }*/

}
