package com.coinz.app.activities


import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*

import com.coinz.app.R

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`


/**
 * Perform an initial login into the app and check whether the app correctly asks for location
 * permissions.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class InitialLoginPermissionRequestTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION")

    @Test
    fun initialLoginPermissionRequestTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatEditText = onView(
                allOf(withId(R.id.log_in_email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.log_in_email_layout),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText.perform(click())

        val appCompatEditText2 = onView(
                allOf(withId(R.id.log_in_email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.log_in_email_layout),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText2.perform(replaceText("ilp@tes"), closeSoftKeyboard())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatEditText3 = onView(
                allOf(withId(R.id.log_in_email), withText("ilp@tes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.log_in_email_layout),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText3.perform(replaceText("ilp@test.com"))

        val appCompatEditText4 = onView(
                allOf(withId(R.id.log_in_email), withText("ilp@test.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.log_in_email_layout),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText4.perform(closeSoftKeyboard())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatEditText5 = onView(
                allOf(withId(R.id.log_in_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.log_in_password_layout),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText5.perform(replaceText("123456"), closeSoftKeyboard())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatButton = onView(
                allOf(withId(R.id.log_in_button), withText("Log In"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.CoordinatorLayout")),
                                        0),
                                4),
                        isDisplayed()))
        appCompatButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        /* ISSUE: Can't resolve com.android, but it's generated code so I don't know what is going
                  wrong.
        val frameLayout = onView(
                allOf(withId(com.android.packageinstaller.R.id.desc_container),
                        childAtPosition(
                                allOf(withId(com.android.packageinstaller.R.id.dialog_container),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.ScrollView::class.java),
                                                0)),
                                0),
                        isDisplayed()))
        frameLayout.check(matches(isDisplayed()))
        */
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
