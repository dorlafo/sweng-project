package ch.epfl.sweng.jassatepfl;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public final class InvitePlayerToMatchActivityTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<InvitePlayerToMatchActivity> activityRule =
            new ActivityTestRule<>(InvitePlayerToMatchActivity.class, false);

    @Before
    public void addPlayersToMock() {
        dbRefWrapTest.reset();
        dbRefWrapTest.addPlayers(DummyDataTest.players());
    }

    @Test
    public void testInviteWithNoPlayerSelected() {
        onView(withId(R.id.invite_button)).check(matches(isDisplayed()));
    }

}
