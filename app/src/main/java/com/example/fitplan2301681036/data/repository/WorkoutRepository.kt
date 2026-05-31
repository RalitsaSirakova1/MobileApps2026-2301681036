package com.example.fitplan2301681036.data.repository

import android.content.Context
import com.example.fitplan2301681036.data.local.FitPlanDatabaseHelper
import com.example.fitplan2301681036.data.model.Workout

class WorkoutRepository(context: Context) {

    private val databaseHelper = FitPlanDatabaseHelper(context.applicationContext)

    fun addWorkout(workout: Workout): Long {
        return databaseHelper.insertWorkout(workout)
    }

    fun getAllWorkouts(): List<Workout> {
        return databaseHelper.getAllWorkouts()
    }

    fun getWorkoutById(id: Long): Workout? {
        return databaseHelper.getWorkoutById(id)
    }

    fun updateWorkout(workout: Workout): Int {
        return databaseHelper.updateWorkout(workout)
    }

    fun deleteWorkout(id: Long): Int {
        return databaseHelper.deleteWorkout(id)
    }
}