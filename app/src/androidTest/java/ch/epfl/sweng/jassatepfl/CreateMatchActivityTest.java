package ch.epfl.sweng.jassatepfl;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.contrib.PickerActions;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.test_utils.ToastMatcherTest;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.jassatepfl.model.Match.GameVariant.CHIBRE;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.amaury;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.jimmy;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Test class for CreateMatchActivity
 *
 */
public final class CreateMatchActivityTest extends InjectedBaseActivityTest {

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.FRENCH);

    public CreateMatchActivityTest() {
        super(CreateMatchActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Set<Player> playerSet = new HashSet<>();
        playerSet.add(amaury);
        playerSet.add(jimmy);
        dbRefWrapTest.addPlayers(playerSet);
        getActivity();
    }

    @Test
    public void testSwitchToInvitePlayerActivity() {
        onView(withId(R.id.add_player_button)).perform(click());
        onView(withId(R.id.invite_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpinnerSelection() {
        onView(withId(R.id.variant_spinner)).perform(click());
        onData(allOf(is(instanceOf(Match.GameVariant.class)), is(CHIBRE))).perform(click());
    }

    @Test
    public void testInputDescription() {
        onView(withId(R.id.description_match_text)).check(matches(withHint(R.string.create_field_description)));
        onView(withId(R.id.description_match_text)).perform(typeText("Hello World"));
        onView(withId(R.id.description_match_text)).check(matches(withText("Hello World")));
    }

    @Test
    public void testPrivacySwitch() {
        onView(withId(R.id.switch_private)).perform(click());
        onView(withId(R.id.switch_private)).check(matches(isChecked()));
    }

    @Test
    public void testTimePickerSetsTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(HOUR_OF_DAY, 1);
        calendar.add(MINUTE, 5);
        setTime(calendar.get(HOUR_OF_DAY), calendar.get(MINUTE));
        onView(withId(R.id.current_expiration_time))
                .check(matches(withText(dateFormat.format(calendar.getTimeInMillis()))));
    }

    @Test
    public void testTimePickerDisplaysToastForInvalidTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(MINUTE, -5);
        setDate(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH));
        setTime(calendar.get(HOUR_OF_DAY), calendar.get(MINUTE));
        onView(withText(R.string.toast_invalid_hour)).inRoot(new ToastMatcherTest())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testDatePickerSetsDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(HOUR_OF_DAY, 1);
        calendar.add(DAY_OF_MONTH, 3);
        calendar.add(MONTH, 1);
        setDate(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH));
        onView(withId(R.id.current_expiration_time))
                .check(matches(withText(dateFormat.format(calendar.getTimeInMillis()))));
    }

    @Test
    public void testDatePickerDisplaysToastForInvalidDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(DAY_OF_MONTH, -5);
        setDate(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH));
        onView(withText(R.string.toast_invalid_date)).inRoot(new ToastMatcherTest())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testDatePickerSetsHourWhenConflictWithCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(YEAR);
        int currentMonth = calendar.get(MONTH);
        int currentDay = calendar.get(DAY_OF_MONTH);
        calendar.add(DAY_OF_MONTH, 6);
        setDate(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH));
        calendar.add(MINUTE, -5);
        setTime(calendar.get(HOUR_OF_DAY), calendar.get(MINUTE));
        setDate(currentYear, currentMonth, currentDay);
        calendar = Calendar.getInstance();
        onView(withId(R.id.current_expiration_time))
                .check(matches(withText(dateFormat.format(calendar.getTimeInMillis()))));
    }

    @Test
    public void testInvitePlayerResultIsHandled() {
        setInviteResultIntent();
        intended(hasComponent(InvitePlayerToMatchActivity.class.getName()));
        checkIfPlayerIsDisplayed(amaury);
        checkIfPlayerIsDisplayed(jimmy);
        release();
    }

    @Test
    public void testRemoveInvitedPlayer() {
        setInviteResultIntent();
        removePlayer(jimmy);
        onView(withText(jimmy.toString())).check(doesNotExist());
        checkIfPlayerIsDisplayed(amaury);
        removePlayer(amaury);
        onView(withText(amaury.toString())).check(doesNotExist());
        release();
    }

    @Test
    public void testLocationPickerButtonSendsIntent() {
        init();
        Intent resultData = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, resultData);
        intending(toPackage("com.google.android.gms")).respondWith(result);

        onView(withId(R.id.create_place_picker_button)).perform(click());
        intended(toPackage("com.google.android.gms"));
        release();
    }

    private void removePlayer(Player player) {
        onData(allOf(is(instanceOf(Player.class)), hasToString(player.toString())))
                .inAdapterView(withId(R.id.create_player_list))
                .perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void checkIfPlayerIsDisplayed(Player player) {
        onData(allOf(is(instanceOf(Player.class)), hasToString(player.toString())))
                .inAdapterView(withId(R.id.create_player_list))
                .check(matches(isDisplayed()));
    }

    private void setInviteResultIntent() {
        init();
        Intent resultData = new Intent();
        resultData.putExtra("player0", "888888");
        resultData.putExtra("player1", "235400");
        resultData.putExtra("players_added", 2);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasComponent(InvitePlayerToMatchActivity.class.getName())).respondWith(result);
        onView(withId(R.id.add_player_button)).perform(click());
    }

    private void setTime(int hour, int minute) {
        onView(withId(R.id.time_picker_button)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hour, minute));
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void setDate(int year, int month, int dayOfMonth) {
        onView(withId(R.id.date_picker_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month + 1, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }

}
