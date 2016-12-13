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
 * Test Class for LoginActivity
 *
 * @author Alexis Montavon
 */
@RunWith(AndroidJUnit4.class)
public final class LoginActivityTest extends InjectedBaseActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityRule =
            new ActivityTestRule<>(LoginActivity.class, false);

    @Test
    public void testShowLoginButtonAnd() {
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
        onView(withId(R.id.login_text_view)).check(matches(isDisplayed()));
    }

    /*@Test
    public void testPressButtonGoesToWebView(){
        getActivity();
        try {
            onView(withId(R.id.login_button)).perform(click());
            //Thread.sleep(3000);
            onView(withText("English")).check(matches(isDisplayed()));
        } catch(Exception e) {
            fail();
        }

    }*/
}
