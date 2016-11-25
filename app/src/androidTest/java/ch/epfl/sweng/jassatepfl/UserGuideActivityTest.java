package ch.epfl.sweng.jassatepfl;

import org.junit.Test;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Alexis Montavon
 *
 * Test class for UserGuideActivity
 */
public class UserGuideActivityTest extends InjectedBaseActivityTest {

    public UserGuideActivityTest() {
        super(UserGuideActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    @Test
    public void testDisplay() {
        onView(withId(R.id.user_guide_text)).check(matches(isDisplayed()));
    }
}
