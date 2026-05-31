package com.example.fitplan2301681036

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fitplan2301681036.data.model.Workout
import com.example.fitplan2301681036.data.repository.WorkoutRepository
import com.example.fitplan2301681036.util.FullscreenUtils
import com.example.fitplan2301681036.util.WorkoutTextBuilder
import com.example.fitplan2301681036.util.WorkoutValidator
import com.example.fitplan2301681036.viewmodel.WorkoutViewModel
import com.example.fitplan2301681036.viewmodel.WorkoutViewModelFactory

class AddEditWorkoutActivity : AppCompatActivity() {

    private lateinit var viewModel: WorkoutViewModel

    private lateinit var etWorkoutName: EditText
    private lateinit var etWorkoutCategory: EditText
    private lateinit var etWorkoutDuration: EditText
    private lateinit var etWorkoutExercises: EditText
    private lateinit var etWorkoutNotes: EditText
    private lateinit var btnSaveWorkout: Button
    private lateinit var btnShareWorkout: Button
    private lateinit var btnQrWorkout: Button
    private lateinit var btnDeleteWorkout: Button

    private var workoutId: Long = -1L
    private var createdAt: Long = 0L
    private var isEditMode: Boolean = false
    private var currentWorkout: Workout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FullscreenUtils.enableFullscreen(this)

        setContentView(R.layout.activity_add_edit_workout)

        setupViewModel()
        bindViews()
        checkEditMode()
        setupButtons()
    }

    private fun setupViewModel() {
        val repository = WorkoutRepository(applicationContext)
        val factory = WorkoutViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[WorkoutViewModel::class.java]
    }

    private fun bindViews() {
        etWorkoutName = findViewById(R.id.etWorkoutName)
        etWorkoutCategory = findViewById(R.id.etWorkoutCategory)
        etWorkoutDuration = findViewById(R.id.etWorkoutDuration)
        etWorkoutExercises = findViewById(R.id.etWorkoutExercises)
        etWorkoutNotes = findViewById(R.id.etWorkoutNotes)
        btnSaveWorkout = findViewById(R.id.btnSaveWorkout)
        btnShareWorkout = findViewById(R.id.btnShareWorkout)
        btnQrWorkout = findViewById(R.id.btnQrWorkout)
        btnDeleteWorkout = findViewById(R.id.btnDeleteWorkout)
    }

    private fun checkEditMode() {
        workoutId = intent.getLongExtra(EXTRA_WORKOUT_ID, -1L)

        if (workoutId != -1L) {
            isEditMode = true
            title = "Edit Workout"
            btnSaveWorkout.text = "Update Workout"
            btnShareWorkout.visibility = View.VISIBLE
            btnQrWorkout.visibility = View.VISIBLE
            btnDeleteWorkout.visibility = View.VISIBLE
            loadWorkoutData()
        } else {
            title = "Add Workout"
            btnSaveWorkout.text = "Save Workout"
            btnShareWorkout.visibility = View.GONE
            btnQrWorkout.visibility = View.GONE
            btnDeleteWorkout.visibility = View.GONE
        }
    }

    private fun loadWorkoutData() {
        viewModel.loadWorkoutById(workoutId)

        viewModel.selectedWorkout.observe(this) { workout ->
            if (workout != null) {
                currentWorkout = workout
                createdAt = workout.createdAt

                etWorkoutName.setText(workout.name)
                etWorkoutCategory.setText(workout.category)
                etWorkoutDuration.setText(workout.durationMinutes.toString())
                etWorkoutExercises.setText(workout.exercises)
                etWorkoutNotes.setText(workout.notes)
            }
        }
    }

    private fun setupButtons() {
        btnSaveWorkout.setOnClickListener {
            saveWorkout()
        }

        btnShareWorkout.setOnClickListener {
            shareWorkout()
        }

        btnQrWorkout.setOnClickListener {
            openQrCodeScreen()
        }

        btnDeleteWorkout.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun saveWorkout() {
        val name = etWorkoutName.text.toString().trim()
        val category = etWorkoutCategory.text.toString().trim()
        val durationText = etWorkoutDuration.text.toString().trim()
        val exercises = etWorkoutExercises.text.toString().trim()
        val notes = etWorkoutNotes.text.toString().trim()

        val isValid = WorkoutValidator.isWorkoutInputValid(
            name = name,
            category = category,
            durationText = durationText,
            exercises = exercises
        )

        if (!isValid) {
            Toast.makeText(
                this,
                "Please fill all required fields correctly",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val duration = WorkoutValidator.parseDuration(durationText)

        if (duration == null) {
            Toast.makeText(this, "Duration must be a positive number", Toast.LENGTH_SHORT).show()
            return
        }

        val success = if (isEditMode) {
            viewModel.updateWorkout(
                id = workoutId,
                name = name,
                category = category,
                durationMinutes = duration,
                exercises = exercises,
                notes = notes,
                createdAt = createdAt
            )
        } else {
            viewModel.addWorkout(
                name = name,
                category = category,
                durationMinutes = duration,
                exercises = exercises,
                notes = notes
            )
        }

        if (success) {
            val message = if (isEditMode) "Workout updated" else "Workout added"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Operation failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareWorkout() {
        val workout = currentWorkout

        if (workout == null) {
            Toast.makeText(this, "Workout is not loaded yet", Toast.LENGTH_SHORT).show()
            return
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "FitPlan Workout: ${workout.name}")
            putExtra(Intent.EXTRA_TEXT, WorkoutTextBuilder.buildWorkoutText(workout))
        }

        startActivity(Intent.createChooser(shareIntent, "Share workout with"))
    }

    private fun openQrCodeScreen() {
        val workout = currentWorkout

        if (workout == null) {
            Toast.makeText(this, "Workout is not loaded yet", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, QRCodeActivity::class.java).apply {
            putExtra(QRCodeActivity.EXTRA_WORKOUT_NAME, workout.name)
            putExtra(
                QRCodeActivity.EXTRA_WORKOUT_CONTENT,
                WorkoutTextBuilder.buildWorkoutText(workout)
            )
        }

        startActivity(intent)
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete workout")
            .setMessage("Are you sure you want to delete this workout?")
            .setPositiveButton("Delete") { _, _ ->
                deleteWorkout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteWorkout() {
        val success = viewModel.deleteWorkout(workoutId)

        if (success) {
            Toast.makeText(this, "Workout deleted", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_WORKOUT_ID = "extra_workout_id"
    }
}