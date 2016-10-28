package ch.epfl.sweng.project;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import ch.epfl.sweng.project.helpers.mocks.firebase.UserProfileMDR;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public final class UserProfileActivityTest extends ActivityInstrumentationTestCase2<UserProfileActivity> {
    UserProfileMDR uPMDR = new UserProfileMDR();
    public UserProfileActivityTest() {
        super(UserProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testProfileIsDisplayedCorrectly() {
        UserProfileActivity activity = getActivity();
        activity.setReference(uPMDR.provideMockedDatabaseReference());
        assertEquals(activity.mtwPlayerID.getText(), 211603);
    }
}
