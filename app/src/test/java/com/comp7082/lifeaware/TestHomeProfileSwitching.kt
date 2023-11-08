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
class TestHomeProfileSwitching {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        val scenario = launchFragmentInContainer<SignUpFragment>();
        scenario.moveToState(Lifecycle.State.STARTED)
    }
    @Test
    fun testHomeProfileSwitching() {

        val bottomNavigationItemView = onView(
                allOf(withId(R.id.profile), withContentDescription("Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                1),
                        isDisplayed()))
        bottomNavigationItemView.perform(click())

        val button = onView(
                allOf(withId(R.id.medical_button), withText("MEDICAL INFORMATION"),
                        withParent(allOf(withId(R.id.contraint_layout),
                                withParent(withId(R.id.frame_layout)))),
                        isDisplayed()))
        button.check(matches(isDisplayed()))

        val button2 = onView(
                allOf(withId(R.id.medical_button), withText("MEDICAL INFORMATION"),
                        withParent(allOf(withId(R.id.contraint_layout),
                                withParent(withId(R.id.frame_layout)))),
                        isDisplayed()))
        button2.check(doesNotExist())

        val bottomNavigationItemView2 = onView(
                allOf(withId(R.id.home), withContentDescription("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                0),
                        isDisplayed()))
        bottomNavigationItemView2.perform(click())
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
