package com.example.fitplan2301681036

import com.example.fitplan2301681036.data.model.Workout
import com.example.fitplan2301681036.util.WorkoutTextBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkoutTextBuilderTest {

    // Tests that workout text contains the main workout information.
    @Test
    fun workoutText_containsMainWorkoutInformation() {
        val workout = Workout(
            id = 1,
            name = "Push Day",
            category = "Strength",
            durationMinutes = 60,
            exercises = "1. Push Ups - 4 x 15",
            notes = "Focus on form",
            createdAt = 1000L
        )

        val actual = WorkoutTextBuilder.buildWorkoutText(workout)

        assertTrue(actual.contains("FitPlan Workout"))
        assertTrue(actual.contains("Name: Push Day"))
        assertTrue(actual.contains("Category: Strength"))
        assertTrue(actual.contains("Duration: 60 minutes"))
        assertTrue(actual.contains("1. Push Ups - 4 x 15"))
        assertTrue(actual.contains("Focus on form"))
    }

    // Tests that notes are included when they are not empty.
    @Test
    fun workoutText_withNotes_includesNotesSection() {
        val workout = Workout(
            id = 1,
            name = "Leg Day",
            category = "Strength",
            durationMinutes = 75,
            exercises = "1. Squats - 5 x 5",
            notes = "Warm up first",
            createdAt = 1000L
        )

        val actual = WorkoutTextBuilder.buildWorkoutText(workout)

        assertTrue(actual.contains("Notes:"))
        assertTrue(actual.contains("Warm up first"))
    }

    // Tests that notes section is skipped when notes are empty.
    @Test
    fun workoutText_withoutNotes_doesNotIncludeNotesSection() {
        val workout = Workout(
            id = 1,
            name = "Cardio",
            category = "Endurance",
            durationMinutes = 30,
            exercises = "1. Running - 30 min",
            notes = "",
            createdAt = 1000L
        )

        val actual = WorkoutTextBuilder.buildWorkoutText(workout)

        assertFalse(actual.contains("Notes:"))
    }

    // Tests the exact generated text for a workout without notes.
    @Test
    fun workoutText_withoutNotes_returnsExpectedText() {
        val workout = Workout(
            id = 1,
            name = "Cardio",
            category = "Endurance",
            durationMinutes = 30,
            exercises = "1. Running - 30 min",
            notes = "",
            createdAt = 1000L
        )

        val expected = """
            FitPlan Workout
            
            Name: Cardio
            Category: Endurance
            Duration: 30 minutes
            
            Exercises:
            1. Running - 30 min
        """.trimIndent() + "\n"

        val actual = WorkoutTextBuilder.buildWorkoutText(workout)

        assertEquals(expected, actual)
    }
}