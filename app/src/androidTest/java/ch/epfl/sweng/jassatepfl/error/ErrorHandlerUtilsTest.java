package ch.epfl.sweng.jassatepfl.error;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.jassatepfl.GameActivity;
import ch.epfl.sweng.jassatepfl.MainActivity;
import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.InjectedBaseActivityTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Alexis Montavon
 *
 * Test class for ErrorHandlerUtils
 */
@RunWith(AndroidJUnit4.class)
public class ErrorHandlerUtilsTest extends InjectedBaseActivityTest {

    /*public ErrorHandlerUtilsTest() {
        super(MainActivity.class);
    }*/
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, false);

    /*@Override
    public void setUp() throws Exception {
        super.setUp();
        //getActivity();
        //activityRule.getActivity();
    }*/

    @Test
    public void testErrorHandlerUtilsPopUp() {
        /*getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog dialog = ErrorHandlerUtils.sendErrorMessage(getActivity(), R.string.error, R.string.error_cannot_join);
            }
        });*/

        activityRule.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog dialog = ErrorHandlerUtils.sendErrorMessage(activityRule.getActivity(), R.string.error, R.string.error_cannot_join);
            }
        });
        onView(withText("Error!")).check(matches(isDisplayed()));
        onView(withText("Can not join match")).check(matches(isDisplayed()));
    }
}
