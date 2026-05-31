package com.example.fitplan2301681036

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class MainActivityUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // Tests the main user flow: open add screen, create workout, and verify it appears in the list.
    @Test
    fun addWorkout_displaysWorkoutInMainList() {
        val workoutName = "Espresso Workout ${System.currentTimeMillis()}"

        onView(withId(R.id.btnAddWorkout)).perform(click())

        onView(withId(R.id.etWorkoutName))
            .perform(replaceText(workoutName))

        onView(withId(R.id.etWorkoutCategory))
            .perform(replaceText("Strength"))

        onView(withId(R.id.etWorkoutDuration))
            .perform(replaceText("60"))

        onView(withId(R.id.etWorkoutExercises))
            .perform(replaceText("1. Push Ups - 4 x 15\n2. Bench Press - 4 x 10"))

        onView(withId(R.id.etWorkoutNotes))
            .perform(replaceText("Created from Espresso UI test"))

        closeSoftKeyboard()

        onView(withId(R.id.btnSaveWorkout)).perform(click())

        onView(withText(workoutName))
            .check(matches(isDisplayed()))
    }
}