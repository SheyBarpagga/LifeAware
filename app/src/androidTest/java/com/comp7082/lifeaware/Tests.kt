package com.comp7082.lifeaware

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

class Tests {
    @RunWith(AndroidJUnit4::class)
    class MyTestSuite {
        @Test
        fun testEventFragment() {
            val scenario = launchFragmentInContainer<SignUpFragment>();
            scenario.moveToState(Lifecycle.State.STARTED)
            onView(withId(R.id.SignUpText)).check(matches(isDisplayed()))
            onView(withId(R.id.nameText)).perform(typeText("test"))
            onView(withId(R.id.home_text)).check(matches(withText("Home")));
        }
    }
}