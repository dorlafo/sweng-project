package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.view.ViewParent;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Match.Meld;
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
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.FIFTY;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.FOUR_JACKS;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.FOUR_NINE;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.HUNDRED;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.MARRIAGE;
import static ch.epfl.sweng.jassatepfl.model.Match.Meld.THREE_CARDS;
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
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        onView(withId(R.id.score_update_cancel)).check(matches(isDisplayed()));
        int pointsGoal = ownedMatch.getGameVariant().getPointGoal();
        String playingTo = String.format(getInstrumentation().getTargetContext().getResources()
                .getString(R.string.game_text_point_goal), pointsGoal);
        onView(withId(R.id.game_playing_to)).check(matches(withText(playingTo)));
    }

    @Test
    public void testElementsAreHiddenForRegularPlayer() {
        dbRefWrapTest.reset();
        Match threePlayerMatch = DummyDataTest.threePlayersMatch();
        Intent intent = new Intent();
        intent.putExtra("match_Id", threePlayerMatch.getMatchID());
        setActivityIntent(intent);
        Set<Match> matches = new HashSet<>();
        matches.add(threePlayerMatch);
        dbRefWrapTest.addMatches(matches);
        Set<MatchStats> stats = new HashSet<>();
        stats.add(new MatchStats(threePlayerMatch));
        dbRefWrapTest.addStats(stats);
        dbRefWrapTest.addPlayers(DummyDataTest.players());
        getActivity();

        onView(withId(R.id.score_update_cancel)).check(matches(not(isDisplayed())));
        onView(withId(R.id.score_update_1)).check(matches(not(isDisplayed())));
        onView(withId(R.id.score_meld_spinner_2)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testCancelDisplaysToastWhenNoCancelAvailable() {
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        onView(withId(R.id.score_update_cancel)).perform(click());
        onView(withText(R.string.toast_cannot_cancel)).inRoot(new ToastMatcherTest())
                .check(matches(isDisplayed()));
        onView(withId(R.id.score_update_cancel)).check(matches(not(isEnabled())));
    }

    @Test
    public void testUpdateScore() {
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        incrementScore(0, 2);
        checkScoreDisplay("2", "155");
        incrementScore(1, 5);
        checkScoreDisplay("154", "160");
    }

    @Test
    public void testMatchButton() {
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        onView(withId(R.id.score_update_1)).perform(click());
        onView(withId(R.id.score_picker_match)).perform(click());
        checkScoreDisplay("257", "0");
        onView(withId(R.id.score_update_2)).perform(click());
        onView(withId(R.id.score_picker_match)).perform(click());
        checkScoreDisplay("257", "257");
    }

    @Test
    public void testCancelUpdateDoesNotUpdateScore() {
        dbRefWrapTest.reset();
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
    }

    @Test
    public void testCancelLastRoundResetsScore() {
        dbRefWrapTest.reset();
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
    }

    @Test
    public void testDisplayEndOfMatchMessage() {
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        for (int i = 0; i < 4; ++i) {
            onView(withId(R.id.score_update_1)).perform(click());
            onView(withId(R.id.score_picker_match)).perform(click());
        }
        String message = String.format(getInstrumentation().getTargetContext()
                .getResources().getString(R.string.dialog_game_end), "Team 1");
        onView(withText(message)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Test
    public void testAddingMeldUpdatesScore() {
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        addMeld(0, MARRIAGE);
        checkScoreDisplay("20", "0");
        addMeld(1, FOUR_JACKS);
        checkScoreDisplay("20", "200");
    }

    @Test
    public void testCancelLastMeld() {
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        incrementScore(1, 100);
        checkScoreDisplay("57", "100");
        addMeld(0, FOUR_JACKS);
        checkScoreDisplay("257", "100");
        onView(withId(R.id.score_update_cancel)).perform(click());
        checkScoreDisplay("57", "100");
    }

    @Test
    public void testCancelSequenceIsCorrect() {
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        addMeld(0, FOUR_NINE);
        incrementScore(0, 100);
        addMeld(1, FIFTY);
        incrementScore(1, 50);
        addMeld(0, HUNDRED);
        addMeld(0, THREE_CARDS);
        checkScoreDisplay("477", "157");
        onView(withId(R.id.score_update_cancel)).perform(click());
        checkScoreDisplay("457", "157");
        onView(withId(R.id.score_update_cancel)).perform(click());
        checkScoreDisplay("357", "157");
        onView(withId(R.id.score_update_cancel)).perform(click());
        checkScoreDisplay("250", "107");
        onView(withId(R.id.score_update_cancel)).perform(click());
        checkScoreDisplay("250", "57");
        onView(withId(R.id.score_update_cancel)).perform(click());
        checkScoreDisplay("150", "0");
        onView(withId(R.id.score_update_cancel)).perform(click());
        checkScoreDisplay("0", "0");
    }

    @Test
    public void testCorrectWinnerIsDisplayedWhenBothTeamsHaveReachedGoal() {
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        for (int i = 0; i < 3; ++i) {
            onView(withId(R.id.score_update_1)).perform(click());
            onView(withId(R.id.score_picker_match)).perform(click());
            onView(withId(R.id.score_update_2)).perform(click());
            onView(withId(R.id.score_picker_match)).perform(click());
        }
        addMeld(0, FOUR_JACKS);
        addMeld(1, FOUR_JACKS);
        incrementScore(1, 50);
        String message = String.format(getInstrumentation().getTargetContext()
                .getResources().getString(R.string.dialog_game_end), "Team 2");
        onView(withText(message)).check(matches(isDisplayed()));
    }

    @Test
    public void testHistoryDisplay() {
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        onView(withId(R.id.score_display_1)).perform(click());
        onView(withId(R.id.score_table_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testHistoryIsCorrect() {
        dbRefWrapTest.reset();
        ownedMatchSetUp();
        getActivity();
        incrementScore(1, 50);
        incrementScore(0, 7);
        addMeld(1, THREE_CARDS);
        onView(withId(R.id.score_display_2)).perform(click());
        onView(atPositionInTable(1, 1)).check(matches(withText("50")));
        onView(atPositionInTable(1, 2)).check(matches(withText("150")));
        onView(atPositionInTable(2, 3)).check(matches(withText(THREE_CARDS.toString())));
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

    private void addMeld(int teamIndex, Meld meld) {
        int meldSpinner = teamIndex == 0 ? R.id.score_meld_spinner_1 : R.id.score_meld_spinner_2;
        onView(withId(meldSpinner)).perform(click());
        onData(allOf(is(instanceOf(Meld.class)), is(meld))).perform(click());
    }

    private void ownedMatchSetUp() {
        Intent intent = new Intent();
        intent.putExtra("match_Id", ownedMatch.getMatchID());
        setActivityIntent(intent);
        Set<Match> matches = new HashSet<>();
        matches.add(ownedMatch);
        dbRefWrapTest.addMatches(matches);
        Set<MatchStats> stats = new HashSet<>();
        stats.add(new MatchStats(ownedMatch));
        dbRefWrapTest.addStats(stats);
        dbRefWrapTest.addPlayers(DummyDataTest.players());
    }

    private static Matcher<View> atPositionInTable(final int x, final int y) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is at position # " + x + " , " + y);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent viewParent = view.getParent();
                if (!(viewParent instanceof TableRow)) {
                    return false;
                }
                TableRow row = (TableRow) viewParent;
                TableLayout table = (TableLayout) row.getParent();
                if (table.indexOfChild(row) != y)
                    return false;
                if (row.indexOfChild(view) == x)
                    return true;
                else
                    return false;
            }
        };
    }

}
