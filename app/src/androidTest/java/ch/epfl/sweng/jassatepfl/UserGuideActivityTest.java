package ch.epfl.sweng.jassatepfl;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Alexis Montavon
 *
 * Test class for UserGuideActivity
 */
@RunWith(AndroidJUnit4.class)
public class UserGuideActivityTest extends InjectedBaseActivityTest {

    /*public UserGuideActivityTest() {
        super(UserGuideActivity.class);
    }*/

    @Rule
    public ActivityTestRule<UserGuideActivity> activityRule =
            new ActivityTestRule<>(UserGuideActivity.class, false);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //getActivity();
        //activityRule.getActivity();
    }

    @Test
    public void testDisplay() {
        onView(withId(R.id.user_guide_text)).check(matches(isDisplayed()));
    }
}
