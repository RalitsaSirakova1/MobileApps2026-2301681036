package com.example.fitplan2301681036.util

import com.example.fitplan2301681036.data.model.Workout

object WorkoutTextBuilder {

    fun buildWorkoutText(workout: Workout): String {
        return buildString {
            appendLine("FitPlan Workout")
            appendLine()
            appendLine("Name: ${workout.name}")
            appendLine("Category: ${workout.category}")
            appendLine("Duration: ${workout.durationMinutes} minutes")
            appendLine()
            appendLine("Exercises:")
            appendLine(workout.exercises)

            if (workout.notes.isNotBlank()) {
                appendLine()
                appendLine("Notes:")
                appendLine(workout.notes)
            }
        }
    }
}