package com.bc.bookcrossing.src;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp(){

    }

    @After
    public void tearDown(){

    }

    public static ViewAction setTextInTextView(final String value){
        return new ViewAction() {
            @SuppressWarnings("unchecked")
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TextView.class));
//                                            ^^^^^^^^^^^^^^^^^^^
// To check that the found view is TextView or it's subclass like EditText
// so it will work for TextView and it's descendants
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((TextView) view).setText(value);
            }

            @Override
            public String getDescription() {
                return "replace text";
            }
        };
    }

    @Test
    public void bookRegistrationManualTestFail(){
        Globals.isLoggedIn = true;
        Globals.usernameLoggedIn = "A";
        onView(withId(R.id.navigation)).check(matches(isDisplayed()));
        onView(withId(R.id.book_registration)).perform(click());
        onView(withId(R.id.book_registration)).check(matches(isDisplayed()));
        onView(withId(R.id.titleBook)).check(matches(isDisplayed()));
        onView(withId(R.id.authorBook)).check(matches(isDisplayed()));
        onView(withId(R.id.Year_of_pubblication)).check(matches(isDisplayed()));
        onView(withId(R.id.EditionNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.BookTypeSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.SubmitBookReg)).check(matches(isDisplayed()));

        onView(withId(R.id.titleBook)).perform(setTextInTextView("CasoDiTestLibroEspresso"));
        onView(withId(R.id.authorBook)).perform(setTextInTextView("Test"));
        onView(withId(R.id.Year_of_pubblication)).perform(setTextInTextView("1"));
        onView(withId(R.id.EditionNumber)).perform(setTextInTextView("1"));
        onView(withId(R.id.BookTypeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Horror"))).perform(click());
        onView(withId(R.id.BookTypeSpinner)).check(matches(withSpinnerText(containsString("Horror"))));

        onView(withId(R.id.SubmitBookReg)).perform(click());

        onView(withText("Problem with Server connection!")).inRoot(withDecorView(not(mainActivityActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void bookRegistrationManualTestSuccess(){
        Globals.isLoggedIn = true;
        Globals.usernameLoggedIn = "A";
        onView(withId(R.id.navigation)).check(matches(isDisplayed()));
        onView(withId(R.id.book_registration)).perform(click());
        onView(withId(R.id.book_registration)).check(matches(isDisplayed()));

        ViewInteraction checkTitle = onView(withId(R.id.titleBook));
        ViewInteraction checkBook = onView(withId(R.id.authorBook));
        ViewInteraction checkYear = onView(withId(R.id.Year_of_pubblication));
        ViewInteraction checkEdition = onView(withId(R.id.EditionNumber));
        ViewInteraction checkType = onView(withId(R.id.BookTypeSpinner));
        ViewInteraction checkBtn = onView(withId(R.id.SubmitBookReg));

        checkTitle.check(matches(isDisplayed()));
        checkBook.check(matches(isDisplayed()));
        checkYear.check(matches(isDisplayed()));
        checkEdition.check(matches(isDisplayed()));
        checkType.check(matches(isDisplayed()));
        checkBtn.check(matches(isDisplayed()));

        checkTitle.check(matches(withText("")));
        checkBook.check(matches(withText("")));
        checkYear.check(matches(withText("")));
        checkEdition.check(matches(withText("")));
        checkType.check(matches(withSpinnerText("")));

        checkTitle.perform(setTextInTextView("CasoDiTestLibroEspresso"));
        checkBook.perform(setTextInTextView("Test"));
        checkYear.perform(setTextInTextView("1"));
        checkEdition.perform(setTextInTextView("1"));
        checkType.perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Horror"))).perform(click());
        checkType.check(matches(withSpinnerText(containsString("Horror"))));

        checkBtn.perform(click());

        checkTitle.check(matches(withText("")));
        checkBook.check(matches(withText("")));
        checkYear.check(matches(withText("")));
        checkEdition.check(matches(withText("")));
        checkType.check(matches(withSpinnerText("")));

    }



}
