package com.example.fitplanproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: WorkoutDatabaseHelper

    private lateinit var btnAddWorkout: Button
    private lateinit var btnViewStats: Button
    private lateinit var tvEmptyState: TextView
    private lateinit var workoutsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = WorkoutDatabaseHelper(this)

        btnAddWorkout = findViewById(R.id.btnAddWorkout)
        btnViewStats = findViewById(R.id.btnViewStats)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        workoutsContainer = findViewById(R.id.workoutsContainer)

        btnAddWorkout.setOnClickListener {
            val intent = Intent(this, AddEditWorkoutActivity::class.java)
            startActivity(intent)
        }

        btnViewStats.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadWorkouts()
    }

    private fun loadWorkouts() {
        workoutsContainer.removeAllViews()

        val workouts = databaseHelper.getAllWorkouts()

        if (workouts.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            return
        }

        tvEmptyState.visibility = View.GONE

        for (workout in workouts) {
            val workoutView = createWorkoutView(workout)
            workoutsContainer.addView(workoutView)
        }
    }

    private fun createWorkoutView(workout: Workout): View {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 20, 24, 20)
            setBackgroundColor(0xFFFFFFFF.toInt())

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(0, 0, 0, 18)
            layoutParams = params
        }

        val nameText = TextView(this).apply {
            text = workout.name
            textSize = 20f
            setTextColor(0xFF111111.toInt())
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val exerciseText = TextView(this).apply {
            text = "Exercise: ${workout.exercise}"
            textSize = 15f
            setTextColor(0xFF555555.toInt())
            setPadding(0, 8, 0, 0)
        }

        val detailsText = TextView(this).apply {
            text = "${workout.duration} min • ${workout.calories} kcal"
            textSize = 15f
            setTextColor(0xFF777777.toInt())
            setPadding(0, 6, 0, 0)
        }

        card.addView(nameText)
        card.addView(exerciseText)
        card.addView(detailsText)

        card.setOnClickListener {
            val intent = Intent(this, AddEditWorkoutActivity::class.java)
            intent.putExtra("workout_id", workout.id)
            startActivity(intent)
        }

        return card
    }
}