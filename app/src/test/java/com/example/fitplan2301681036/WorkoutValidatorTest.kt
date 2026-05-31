package com.example.fitplan2301681036

import com.example.fitplan2301681036.util.WorkoutValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkoutValidatorTest {

    // Tests that valid workout input is accepted.
    @Test
    fun validWorkoutInput_returnsTrue() {
        val actual = WorkoutValidator.isWorkoutInputValid(
            name = "Push Day",
            category = "Strength",
            durationText = "60",
            exercises = "1. Push Ups - 4 x 15"
        )

        assertTrue(actual)
    }

    // Tests that an empty workout name is rejected.
    @Test
    fun emptyWorkoutName_returnsFalse() {
        val actual = WorkoutValidator.isWorkoutInputValid(
            name = "",
            category = "Strength",
            durationText = "60",
            exercises = "1. Push Ups - 4 x 15"
        )

        assertFalse(actual)
    }

    // Tests that an empty category is rejected.
    @Test
    fun emptyCategory_returnsFalse() {
        val actual = WorkoutValidator.isWorkoutInputValid(
            name = "Push Day",
            category = "",
            durationText = "60",
            exercises = "1. Push Ups - 4 x 15"
        )

        assertFalse(actual)
    }

    // Tests that empty exercises are rejected.
    @Test
    fun emptyExercises_returnsFalse() {
        val actual = WorkoutValidator.isWorkoutInputValid(
            name = "Push Day",
            category = "Strength",
            durationText = "60",
            exercises = ""
        )

        assertFalse(actual)
    }

    // Tests that text duration is rejected.
    @Test
    fun textDuration_returnsFalse() {
        val actual = WorkoutValidator.isWorkoutInputValid(
            name = "Push Day",
            category = "Strength",
            durationText = "abc",
            exercises = "1. Push Ups - 4 x 15"
        )

        assertFalse(actual)
    }

    // Tests that zero duration is rejected.
    @Test
    fun zeroDuration_returnsFalse() {
        val actual = WorkoutValidator.isWorkoutInputValid(
            name = "Push Day",
            category = "Strength",
            durationText = "0",
            exercises = "1. Push Ups - 4 x 15"
        )

        assertFalse(actual)
    }

    // Tests that valid duration text is parsed correctly.
    @Test
    fun validDurationText_returnsDurationNumber() {
        val actual = WorkoutValidator.parseDuration("45")

        assertEquals(45, actual)
    }

    // Tests that invalid duration text returns null.
    @Test
    fun invalidDurationText_returnsNull() {
        val actual = WorkoutValidator.parseDuration("wrong")

        assertNull(actual)
    }

    // Tests that negative duration returns null.
    @Test
    fun negativeDuration_returnsNull() {
        val actual = WorkoutValidator.parseDuration("-10")

        assertNull(actual)
    }
}