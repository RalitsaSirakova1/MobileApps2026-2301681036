package com.example.fitplan2301681036.util

object WorkoutValidator {

    fun isWorkoutInputValid(
        name: String,
        category: String,
        durationText: String,
        exercises: String
    ): Boolean {
        return name.trim().isNotEmpty()
                && category.trim().isNotEmpty()
                && durationText.trim().isNotEmpty()
                && exercises.trim().isNotEmpty()
                && durationText.toIntOrNull() != null
                && durationText.toInt() > 0
    }

    fun parseDuration(durationText: String): Int? {
        val duration = durationText.trim().toIntOrNull()
        return if (duration != null && duration > 0) duration else null
    }
}