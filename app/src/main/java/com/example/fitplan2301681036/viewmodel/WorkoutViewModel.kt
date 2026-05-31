package com.example.fitplan2301681036.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitplan2301681036.data.model.Workout
import com.example.fitplan2301681036.data.repository.WorkoutRepository

class WorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    private val _selectedWorkout = MutableLiveData<Workout?>()
    val selectedWorkout: LiveData<Workout?> = _selectedWorkout

    fun loadWorkouts() {
        _workouts.value = repository.getAllWorkouts()
    }

    fun loadWorkoutById(id: Long) {
        _selectedWorkout.value = repository.getWorkoutById(id)
    }

    fun addWorkout(
        name: String,
        category: String,
        durationMinutes: Int,
        exercises: String,
        notes: String
    ): Boolean {
        val workout = Workout(
            name = name,
            category = category,
            durationMinutes = durationMinutes,
            exercises = exercises,
            notes = notes
        )

        val result = repository.addWorkout(workout)
        loadWorkouts()

        return result != -1L
    }

    fun updateWorkout(
        id: Long,
        name: String,
        category: String,
        durationMinutes: Int,
        exercises: String,
        notes: String,
        createdAt: Long
    ): Boolean {
        val workout = Workout(
            id = id,
            name = name,
            category = category,
            durationMinutes = durationMinutes,
            exercises = exercises,
            notes = notes,
            createdAt = createdAt
        )

        val result = repository.updateWorkout(workout)
        loadWorkouts()

        return result > 0
    }

    fun deleteWorkout(id: Long): Boolean {
        val result = repository.deleteWorkout(id)
        loadWorkouts()

        return result > 0
    }
}