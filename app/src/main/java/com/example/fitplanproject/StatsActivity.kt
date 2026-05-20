package com.example.fitplanproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StatsActivity : AppCompatActivity() {

    private lateinit var databaseHelper: WorkoutDatabaseHelper

    private lateinit var tvTotalWorkouts: TextView
    private lateinit var tvTotalDuration: TextView
    private lateinit var tvTotalCalories: TextView
    private lateinit var tvAverageCalories: TextView
    private lateinit var tvLongestWorkout: TextView
    private lateinit var btnBackFromStats: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        databaseHelper = WorkoutDatabaseHelper(this)

        tvTotalWorkouts = findViewById(R.id.tvTotalWorkouts)
        tvTotalDuration = findViewById(R.id.tvTotalDuration)
        tvTotalCalories = findViewById(R.id.tvTotalCalories)
        tvAverageCalories = findViewById(R.id.tvAverageCalories)
        tvLongestWorkout = findViewById(R.id.tvLongestWorkout)
        btnBackFromStats = findViewById(R.id.btnBackFromStats)

        loadStatistics()

        btnBackFromStats.setOnClickListener {
            finish()
        }
    }

    private fun loadStatistics() {
        val workouts = databaseHelper.getAllWorkouts()

        if (workouts.isEmpty()) {
            tvTotalWorkouts.text = "Total workouts: 0"
            tvTotalDuration.text = "Total duration: 0 min"
            tvTotalCalories.text = "Total calories: 0 kcal"
            tvAverageCalories.text = "Average calories: 0 kcal"
            tvLongestWorkout.text = "Longest workout: None"
            return
        }

        val totalWorkouts = workouts.size
        val totalDuration = workouts.sumOf { it.duration }
        val totalCalories = workouts.sumOf { it.calories }
        val averageCalories = totalCalories / totalWorkouts
        val longestWorkout = workouts.maxByOrNull { it.duration }

        tvTotalWorkouts.text = "Total workouts: $totalWorkouts"
        tvTotalDuration.text = "Total duration: $totalDuration min"
        tvTotalCalories.text = "Total calories: $totalCalories kcal"
        tvAverageCalories.text = "Average calories: $averageCalories kcal"
        tvLongestWorkout.text =
            "Longest workout: ${longestWorkout?.name} - ${longestWorkout?.duration} min"
    }
}