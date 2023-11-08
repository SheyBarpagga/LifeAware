package com.comp7082.lifeaware


import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestSignUp {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    @Before
    fun setup() {
        val scenario = launchFragmentInContainer<SignUpFragment>();
        scenario.moveToState(Lifecycle.State.STARTED)
    }
    @Test
    fun testSignUp() {
        val appCompatEditText = onView(
                allOf(withId(R.id.nameText), withText("Name"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout),
                                        0),
                                1),
                        isDisplayed()))
        appCompatEditText.perform(replaceText("test"))

        val appCompatEditText2 = onView(
                allOf(withId(R.id.nameText), withText("test"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout),
                                        0),
                                1),
                        isDisplayed()))
        appCompatEditText2.perform(closeSoftKeyboard())

        val appCompatEditText3 = onView(
                allOf(withId(R.id.emailAddressText), withText("Email"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText3.perform(replaceText("test@test.com"))

        val appCompatEditText4 = onView(
                allOf(withId(R.id.emailAddressText), withText("test@test.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText4.perform(closeSoftKeyboard())

        val appCompatEditText5 = onView(
                allOf(withId(R.id.passwordText), withText("Password"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout),
                                        0),
                                3),
                        isDisplayed()))
        appCompatEditText5.perform(replaceText("test"))

        val appCompatEditText6 = onView(
                allOf(withId(R.id.passwordText), withText("test"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout),
                                        0),
                                3),
                        isDisplayed()))
        appCompatEditText6.perform(closeSoftKeyboard())

        val appCompatEditText7 = onView(
                allOf(withId(R.id.ageText), withText("Age"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout),
                                        0),
                                4),
                        isDisplayed()))
        appCompatEditText7.perform(replaceText("56"))

        val appCompatEditText8 = onView(
                allOf(withId(R.id.ageText), withText("56"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout),
                                        0),
                                4),
                        isDisplayed()))
        appCompatEditText8.perform(closeSoftKeyboard())

        val appCompatEditText9 = onView(
                allOf(withId(R.id.ageText), withText("56"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout),
                                        0),
                                4),
                        isDisplayed()))
        appCompatEditText9.perform(pressImeActionButton())

        val materialButton = onView(
                allOf(withId(R.id.buttonSignUp), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout),
                                        0),
                                6),
                        isDisplayed()))
        materialButton.perform(click())

        val button = onView(
                allOf(withId(R.id.buttonSignUp), withText("NEXT"),
                        withParent(withParent(withId(R.id.frame_layout))),
                        isDisplayed()))
        button.check(doesNotExist())
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
