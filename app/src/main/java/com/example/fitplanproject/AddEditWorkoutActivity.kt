package com.example.fitplanproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditWorkoutActivity : AppCompatActivity() {

    private lateinit var databaseHelper: WorkoutDatabaseHelper

    private lateinit var tvScreenTitle: TextView
    private lateinit var tvScreenSubtitle: TextView
    private lateinit var etWorkoutName: EditText
    private lateinit var etExercise: EditText
    private lateinit var etDuration: EditText
    private lateinit var etCalories: EditText
    private lateinit var btnSaveWorkout: Button
    private lateinit var btnDeleteWorkout: Button
    private lateinit var btnShareWorkout: Button
    private lateinit var btnBack: Button

    private var workoutId: Int = -1
    private var isEditMode: Boolean = false
    private var currentWorkout: Workout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_workout)

        databaseHelper = WorkoutDatabaseHelper(this)

        tvScreenTitle = findViewById(R.id.tvScreenTitle)
        tvScreenSubtitle = findViewById(R.id.tvScreenSubtitle)
        etWorkoutName = findViewById(R.id.etWorkoutName)
        etExercise = findViewById(R.id.etExercise)
        etDuration = findViewById(R.id.etDuration)
        etCalories = findViewById(R.id.etCalories)
        btnSaveWorkout = findViewById(R.id.btnSaveWorkout)
        btnDeleteWorkout = findViewById(R.id.btnDeleteWorkout)
        btnShareWorkout = findViewById(R.id.btnShareWorkout)
        btnBack = findViewById(R.id.btnBack)

        workoutId = intent.getIntExtra("workout_id", -1)
        isEditMode = workoutId != -1

        if (isEditMode) {
            setupEditMode()
        } else {
            setupAddMode()
        }

        btnSaveWorkout.setOnClickListener {
            saveWorkout()
        }

        btnDeleteWorkout.setOnClickListener {
            deleteWorkout()
        }

        btnShareWorkout.setOnClickListener {
            shareWorkout()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupAddMode() {
        tvScreenTitle.text = "Add Workout"
        tvScreenSubtitle.text = "Create your training plan"
        btnSaveWorkout.text = "Save Workout"
        btnDeleteWorkout.visibility = View.GONE
        btnShareWorkout.visibility = View.GONE
    }

    private fun setupEditMode() {
        tvScreenTitle.text = "Edit Workout"
        tvScreenSubtitle.text = "Update or delete your training plan"
        btnSaveWorkout.text = "Save Changes"
        btnDeleteWorkout.visibility = View.VISIBLE
        btnShareWorkout.visibility = View.VISIBLE

        currentWorkout = databaseHelper.getWorkoutById(workoutId)

        if (currentWorkout == null) {
            Toast.makeText(this, "Workout not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        etWorkoutName.setText(currentWorkout!!.name)
        etExercise.setText(currentWorkout!!.exercise)
        etDuration.setText(currentWorkout!!.duration.toString())
        etCalories.setText(currentWorkout!!.calories.toString())
    }

    private fun saveWorkout() {
        val name = etWorkoutName.text.toString().trim()
        val exercise = etExercise.text.toString().trim()
        val durationText = etDuration.text.toString().trim()
        val caloriesText = etCalories.text.toString().trim()

        if (name.isEmpty() || exercise.isEmpty() || durationText.isEmpty() || caloriesText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val duration = durationText.toIntOrNull()
        val calories = caloriesText.toIntOrNull()

        if (duration == null || calories == null) {
            Toast.makeText(this, "Duration and calories must be numbers", Toast.LENGTH_SHORT).show()
            return
        }

        if (isEditMode) {
            val updatedWorkout = Workout(
                id = workoutId,
                name = name,
                exercise = exercise,
                duration = duration,
                calories = calories
            )

            val isUpdated = databaseHelper.updateWorkout(updatedWorkout)

            if (isUpdated) {
                Toast.makeText(this, "Workout updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update workout", Toast.LENGTH_SHORT).show()
            }
        } else {
            val newWorkout = Workout(
                name = name,
                exercise = exercise,
                duration = duration,
                calories = calories
            )

            val isSaved = databaseHelper.addWorkout(newWorkout)

            if (isSaved) {
                Toast.makeText(this, "Workout saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save workout", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteWorkout() {
        val isDeleted = databaseHelper.deleteWorkout(workoutId)

        if (isDeleted) {
            Toast.makeText(this, "Workout deleted", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Failed to delete workout", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareWorkout() {
        val name = etWorkoutName.text.toString().trim()
        val exercise = etExercise.text.toString().trim()
        val duration = etDuration.text.toString().trim()
        val calories = etCalories.text.toString().trim()

        val shareText = """
            My workout:
            $name
            
            Exercise: $exercise
            Duration: $duration minutes
            Calories: $calories kcal
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "My FitPlan Workout")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(Intent.createChooser(intent, "Share workout"))
    }
}