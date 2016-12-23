package ch.epfl.sweng.jassatepfl;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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

    @Test
    public void testPressButtonGoesToWebView() {
        init();
        Matcher<Intent> expectedIntent = hasComponent(WebViewActivity.class.getName());
        intending(expectedIntent)
                .respondWith(new Instrumentation.ActivityResult(RESULT_CANCELED, null));
        onView(withId(R.id.login_button)).perform(click());
        intended(expectedIntent);
        release();
    }

    // Do more here
    @Test
    public void testWebViewResponseIsHandled() {
        init();
        Matcher<Intent> expectedIntent = hasComponent(WebViewActivity.class.getName());
        intending(expectedIntent)
                .respondWith(new Instrumentation.ActivityResult(RESULT_OK, null));
        onView(withId(R.id.login_button)).perform(click());
        intended(expectedIntent);
        release();
    }

}
