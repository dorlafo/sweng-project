package ch.epfl.sweng.jassatepfl.error;

import android.support.v7.app.AlertDialog;

import org.junit.Test;

import ch.epfl.sweng.jassatepfl.MainActivity;
import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.InjectedBaseActivityTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Alexis Montavon
 *
 * Test class for ErrorHandlerUtils
 */

public class ErrorHandlerUtilsTest extends InjectedBaseActivityTest {

    public ErrorHandlerUtilsTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    @Test
    public void testErrorHandlerUtilsPopUp() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog dialog = ErrorHandlerUtils.sendErrorMessage(getActivity(), R.string.error, R.string.error_cannot_join);
            }
        });
        onView(withText("Error!")).check(matches(isDisplayed()));
        onView(withText("Can not join match")).check(matches(isDisplayed()));
        onView(withText("Can not join match")).perform(pressBack());
    }
}
