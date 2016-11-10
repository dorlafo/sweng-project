package ch.epfl.sweng.jassatepfl;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

/*import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;*/

/**
 * Test class for CreateMatchActivity
 *
 * //TODO: Mock, we can not test until we mock firebase authentication!
 *
 * @author Alexis Montavon
 */
public final class CreateMatchActivityTest extends ActivityInstrumentationTestCase2<CreateMatchActivity> {
    public CreateMatchActivityTest() {
        super(CreateMatchActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    /*@Test
    public void testSwitchToInvitePlayerActivity() {
        getActivity();
        onView(withId(R.id.add_player_button)).perform(click());
        onView(withId(R.id.invite_button)).check(matches(isDisplayed()));
    }*/
}
