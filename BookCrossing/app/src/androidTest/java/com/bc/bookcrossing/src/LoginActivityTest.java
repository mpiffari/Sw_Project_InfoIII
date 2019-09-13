package com.bc.bookcrossing.src;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.Intents;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);


    @Before
    public void setUp(){
        Intents.init();

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
    public void loginTestSuccess() {
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
        onView(withId(R.id.btnSignIn)).check(matches(isDisplayed()));

        onView(withId(R.id.username)).perform(setTextInTextView("A"));
        onView(withId(R.id.password)).perform(setTextInTextView("1"));

        onView(withId(R.id.login_button)).perform(click());

        onView(withText(R.string.login_ok)).inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        onView(withText(R.string.login_ok)).inRoot(withDecorView(not(loginActivityActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(withText("Login completed successfully")));

        intended(hasComponent(MainActivity.class.getName()));
        onView(withId(R.id.leaveButton)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));

    }




}

