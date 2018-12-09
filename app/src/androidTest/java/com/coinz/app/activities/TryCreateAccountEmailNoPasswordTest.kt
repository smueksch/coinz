package com.coinz.app.activities


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.coinz.app.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Attempt to create account with an email address, but not a password and check that app refuses
 * and shows a Toast with appropriate message instead.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class TryCreateAccountEmailNoPasswordTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun tryCreateAccountEmailNoPasswordTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatButton = onView(
                allOf(withId(R.id.log_in_create_account_button), withText("New to Coinz? Create an account!"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.CoordinatorLayout")),
                                        0),
                                5),
                        isDisplayed()))
        appCompatButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatEditText = onView(
                allOf(withId(R.id.create_acc_email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.create_acc_email_layout),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(5000)

        val appCompatEditText2 = onView(
                allOf(withId(R.id.create_acc_email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.create_acc_email_layout),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText2.perform(replaceText("ilp@test.com"), closeSoftKeyboard())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val editText = onView(
                allOf(withId(R.id.create_acc_email), withText("ilp@test.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.create_acc_email_layout),
                                        0),
                                0),
                        isDisplayed()))
        editText.check(matches(withText("ilp@test.com")))

        pressBack()

        val appCompatButton2 = onView(
                allOf(withId(R.id.create_acc_button), withText("Create Account"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.CoordinatorLayout")),
                                        0),
                                4),
                        isDisplayed()))
        appCompatButton2.perform(click())

        val viewGroup = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(android.R.id.content),
                                0),
                        0),
                        isDisplayed()))
        viewGroup.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
