package ch.epfl.sweng.jassatepfl;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Alexis Montavon
 *
 * Test Class for LoginActivity
 */
public final class LoginActivityTest extends InjectedBaseActivityTest {

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testShowLoginButtonAnd() {
        getActivity();
        try {
            onView(withId(R.id.login_button)).check(matches(isDisplayed()));
            onView(withId(R.id.login_text_view)).check(matches(isDisplayed()));
            assertTrue(true);
        }catch(Exception e) {
            fail();
        }

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
