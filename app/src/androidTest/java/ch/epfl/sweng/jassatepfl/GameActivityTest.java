package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.NumberPicker;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;
import ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest;
import ch.epfl.sweng.jassatepfl.test_utils.ToastMatcherTest;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.FOUR_JACKS;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.LAST_TRICK;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public final class GameActivityTest extends InjectedBaseActivityTest {

    private final Match ownedMatch = DummyDataTest.ownedMatch();

    public GameActivityTest() {
        super(GameActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testElementsAreDisplayedForOwner() {
        ownedMatchSetUp();
        getActivity();
        onView(withId(R.id.score_update_cancel)).check(matches(isDisplayed()));
        int pointsGoal = ownedMatch.getGameVariant().getPointGoal();
        String playingTo = String.format(getInstrumentation().getTargetContext().getResources()
                .getString(R.string.game_text_point_goal), pointsGoal);
        onView(withId(R.id.game_playing_to)).check(matches(withText(playingTo)));
        dbRefWrapTest.reset();
    }

    /* TODO: fix this
    @Test
    public void testElementsAreHiddenForRegularPlayer() {
        Match threePlayerMatch = DummyData.threePlayersMatch();
        Intent intent = new Intent();
        intent.putExtra("match_Id", threePlayerMatch.getMatchID());
        setActivityIntent(intent);
        Set<Match> matches = new HashSet<>();
        matches.add(threePlayerMatch);
        dbRefWrapTest.addMatches(matches);
        Set<MatchStats> stats = new HashSet<>();
        stats.add(new MatchStats(threePlayerMatch.getMatchID(), threePlayerMatch.getGameVariant()));
        dbRefWrapTest.addStats(stats);
        getActivity();
        onView(withId(R.id.score_picker_cancel)).check(matches(not(isDisplayed())));
        onView(withId(R.id.score_update_1)).check(matches(not(isDisplayed())));
        onView(withId(R.id.score_meld_spinner_2)).check(matches(not(isDisplayed())));
        dbRefWrapTest.reset();
    }
    */

    @Test
    public void testCancelDisplaysToastWhenNoCancelAvailable() {
        ownedMatchSetUp();
        getActivity();
        onView(withId(R.id.score_update_cancel)).perform(click());
        onView(withText(R.string.toast_cannot_cancel)).inRoot(new ToastMatcherTest())
                .check(matches(isDisplayed()));
        onView(withId(R.id.score_update_cancel)).check(matches(not(isEnabled())));
        dbRefWrapTest.reset();
    }

    @Test
    public void testUpdateScore() {
        ownedMatchSetUp();
        getActivity();
        incrementScore(0, 2);
        checkScoreDisplay("2", "155");
        incrementScore(1, 5);
        checkScoreDisplay("154", "160");
        dbRefWrapTest.reset();
    }

    @Test
    public void testMatchButton() {
        ownedMatchSetUp();
        getActivity();
        onView(withId(R.id.score_update_1)).perform(click());
        onView(withId(R.id.score_picker_match)).perform(click());
        checkScoreDisplay("257", "0");
        onView(withId(R.id.score_update_2)).perform(click());
        onView(withId(R.id.score_picker_match)).perform(click());
        checkScoreDisplay("257", "257");
        dbRefWrapTest.reset();
    }

    @Test
    public void testCancelUpdateDoesNotUpdateScore() {
        ownedMatchSetUp();
        getActivity();
        incrementScore(0, 50);
        checkScoreDisplay("50", "107");
        onView(withId(R.id.score_update_1)).perform(click());
        onView(withId(R.id.score_picker_cancel)).perform(click());
        checkScoreDisplay("50", "107");
        onView(withId(R.id.score_update_2)).perform(click());
        onView(withId(R.id.score_picker_cancel)).perform(click());
        checkScoreDisplay("50", "107");
        dbRefWrapTest.reset();
    }

    @Test
    public void testCancelLastRoundResetsScore() {
        ownedMatchSetUp();
        getActivity();
        incrementScore(0, 60);
        checkScoreDisplay("60", "97");
        incrementScore(1, 33);
        checkScoreDisplay("184", "130");
        onView(withId(R.id.score_update_cancel)).perform(click());
        checkScoreDisplay("60", "97");
        onView(withId(R.id.score_update_cancel)).perform(click());
        checkScoreDisplay("0", "0");
        dbRefWrapTest.reset();
    }

    @Test
    public void testDisplayEndOfMatchMessage() {
        ownedMatchSetUp();
        getActivity();
        for (int i = 0; i < 6; ++i) {
            onView(withId(R.id.score_update_1)).perform(click());
            onView(withId(R.id.score_picker_match)).perform(click());
        }
        String message = String.format(getInstrumentation().getTargetContext()
                .getResources().getString(R.string.dialog_game_end), "Team 1");
        onView(withText(message)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        dbRefWrapTest.reset();
    }

    @Test
    public void testAddingMeldUpdatesScore() {
        ownedMatchSetUp();
        getActivity();
        onView(withId(R.id.score_meld_spinner_1)).perform(click());
        onData(allOf(is(instanceOf(Match.Meld.class)), is(LAST_TRICK))).perform(click());
        checkScoreDisplay("5", "0");
        onView(withId(R.id.score_meld_spinner_2)).perform(click());
        onData(allOf(is(instanceOf(Match.Meld.class)), is(FOUR_JACKS))).perform(click());
        checkScoreDisplay("5", "200");
    }

    @Test
    public void testCancelLastMelds() {
        ownedMatchSetUp();
        getActivity();
        incrementScore(1, 100);
        checkScoreDisplay("57", "100");
        onView(withId(R.id.score_meld_spinner_1)).perform(click());
        onData(allOf(is(instanceOf(Match.Meld.class)), is(FOUR_JACKS))).perform(click());
        checkScoreDisplay("257", "100");
        onView(withId(R.id.score_update_cancel)).perform(click());
        checkScoreDisplay("57", "100");
    }

    private void checkScoreDisplay(String firstDisplay, String secondDisplay) {
        onView(withId(R.id.score_display_1)).check(matches(withText(firstDisplay)));
        onView(withId(R.id.score_display_2)).check(matches(withText(secondDisplay)));
    }

    private void incrementScore(int teamIndex, final int value) {
        int button = teamIndex == 0 ? R.id.score_update_1 : R.id.score_update_2;
        onView(withId(button)).perform(click());
        onView(withClassName(equalTo(NumberPicker.class.getName()))).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(NumberPicker.class);
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                NumberPicker numberPicker = (NumberPicker) view;
                numberPicker.setValue(value);
            }
        });
        onView(withId(R.id.score_picker_confirm)).perform(click());
    }

    private void ownedMatchSetUp() {
        Intent intent = new Intent();
        intent.putExtra("match_Id", ownedMatch.getMatchID());
        setActivityIntent(intent);
        Set<Match> matches = new HashSet<>();
        matches.add(ownedMatch);
        dbRefWrapTest.addMatches(matches);
        Set<MatchStats> stats = new HashSet<>();
        stats.add(new MatchStats(ownedMatch.getMatchID(), ownedMatch.getGameVariant()));
        dbRefWrapTest.addStats(stats);
    }

}
