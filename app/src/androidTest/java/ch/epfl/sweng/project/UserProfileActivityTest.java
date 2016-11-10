package ch.epfl.sweng.project;

import android.widget.TextView;

import org.junit.Test;

import java.lang.reflect.Field;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * UserProfileActivityTest show an example of mock usage
 */
public final class UserProfileActivityTest extends InjectedBaseActivityTest {
    UserProfileActivity act;


    public UserProfileActivityTest() {
        super(UserProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        act = (UserProfileActivity) getActivity();
    }


    /**
     * This test verifies that the method child gets called
     */
    @Test
    public void testMethodChildIsCalledOnRef() {
        verify(dbRefWrapped).child("players");
    }


    /**
     * This test verifies that the assignment to the text view is correct
     * This is an example that uses java reflection in order to access private fields
     */
    @Test
    public void testUserProfileActivity() {
        try {
            Field f = act.getClass().getDeclaredField("mtwFirstName");
            f.setAccessible(true);
            TextView tView = (TextView) f.get(act);
            assertEquals("First name : Bob", tView.getText().toString());
        } catch (Exception e) {
            fail();
        }
    }
}
