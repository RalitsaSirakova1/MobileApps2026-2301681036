package com.example.fitplan2301681036

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fitplan2301681036.data.model.Workout
import com.example.fitplan2301681036.data.repository.WorkoutRepository
import com.example.fitplan2301681036.util.FullscreenUtils
import com.example.fitplan2301681036.viewmodel.WorkoutViewModel
import com.example.fitplan2301681036.viewmodel.WorkoutViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: WorkoutViewModel

    private lateinit var workoutsContainer: LinearLayout
    private lateinit var tvEmptyMessage: TextView
    private lateinit var btnAddWorkout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FullscreenUtils.enableFullscreen(this)

        setContentView(R.layout.activity_main)

        setupViewModel()
        bindViews()
        setupButtons()
        observeWorkouts()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadWorkouts()
    }

    private fun setupViewModel() {
        val repository = WorkoutRepository(applicationContext)
        val factory = WorkoutViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[WorkoutViewModel::class.java]
    }

    private fun bindViews() {
        workoutsContainer = findViewById(R.id.workoutsContainer)
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage)
        btnAddWorkout = findViewById(R.id.btnAddWorkout)
    }

    private fun setupButtons() {
        btnAddWorkout.setOnClickListener {
            val intent = Intent(this, AddEditWorkoutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeWorkouts() {
        viewModel.workouts.observe(this) { workouts ->
            showWorkouts(workouts)
        }
    }

    private fun showWorkouts(workouts: List<Workout>) {
        workoutsContainer.removeAllViews()

        if (workouts.isEmpty()) {
            tvEmptyMessage.visibility = TextView.VISIBLE
            return
        }

        tvEmptyMessage.visibility = TextView.GONE

        workouts.forEach { workout ->
            val card = createWorkoutCard(workout)
            workoutsContainer.addView(card)
        }
    }

    private fun createWorkoutCard(workout: Workout): LinearLayout {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 28, 32, 28)
            background = getDrawable(R.drawable.bg_card)
            isClickable = true
            isFocusable = true

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(0, 0, 0, 24)
            layoutParams = params

            setOnClickListener {
                val intent = Intent(this@MainActivity, AddEditWorkoutActivity::class.java)
                intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_ID, workout.id)
                startActivity(intent)
            }
        }

        val title = TextView(this).apply {
            text = workout.name
            textSize = 21f
            setTextColor(getColor(R.color.text_primary))
            gravity = Gravity.START
            setTypeface(null, Typeface.BOLD)
        }

        val category = TextView(this).apply {
            text = workout.category
            textSize = 15f
            setTextColor(getColor(R.color.accent_purple))
            setPadding(0, 6, 0, 0)
            setTypeface(null, Typeface.BOLD)
        }

        val duration = TextView(this).apply {
            text = "Duration: ${workout.durationMinutes} min"
            textSize = 14f
            setTextColor(getColor(R.color.text_secondary))
            setPadding(0, 12, 0, 0)
        }

        val exercises = TextView(this).apply {
            text = workout.exercises
            textSize = 14f
            setTextColor(getColor(R.color.text_primary))
            setPadding(0, 12, 0, 0)
            maxLines = 4
        }

        val notes = TextView(this).apply {
            text = if (workout.notes.isBlank()) "No notes" else "Notes: ${workout.notes}"
            textSize = 14f
            setTextColor(getColor(R.color.text_secondary))
            setPadding(0, 10, 0, 0)
            maxLines = 2
        }

        card.addView(title)
        card.addView(category)
        card.addView(duration)
        card.addView(exercises)
        card.addView(notes)

        return card
    }
}