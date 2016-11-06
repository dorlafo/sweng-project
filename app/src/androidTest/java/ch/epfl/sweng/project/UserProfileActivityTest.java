package ch.epfl.sweng.project;

import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


public final class UserProfileActivityTest extends InjectedBaseActivityTest {

    public UserProfileActivityTest() {
        super(UserProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    @Test
    public void testProfileIsDisplayedCorrectly() {
        verify(dbRefWrapped).child("players");
    }
}
