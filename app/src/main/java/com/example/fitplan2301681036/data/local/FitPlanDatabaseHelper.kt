package com.example.fitplan2301681036.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fitplan2301681036.data.model.Workout

class FitPlanDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createWorkoutsTable = """
            CREATE TABLE $TABLE_WORKOUTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_CATEGORY TEXT NOT NULL,
                $COLUMN_DURATION_MINUTES INTEGER NOT NULL,
                $COLUMN_EXERCISES TEXT NOT NULL,
                $COLUMN_NOTES TEXT NOT NULL,
                $COLUMN_CREATED_AT INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(createWorkoutsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKOUTS")
        onCreate(db)
    }

    fun insertWorkout(workout: Workout): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, workout.name)
            put(COLUMN_CATEGORY, workout.category)
            put(COLUMN_DURATION_MINUTES, workout.durationMinutes)
            put(COLUMN_EXERCISES, workout.exercises)
            put(COLUMN_NOTES, workout.notes)
            put(COLUMN_CREATED_AT, workout.createdAt)
        }

        return db.insert(TABLE_WORKOUTS, null, values)
    }

    fun getAllWorkouts(): List<Workout> {
        val workouts = mutableListOf<Workout>()
        val db = readableDatabase

        val cursor = db.query(
            TABLE_WORKOUTS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_CREATED_AT DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                workouts.add(cursorToWorkout(it))
            }
        }

        return workouts
    }

    fun getWorkoutById(id: Long): Workout? {
        val db = readableDatabase

        val cursor = db.query(
            TABLE_WORKOUTS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use {
            return if (it.moveToFirst()) {
                cursorToWorkout(it)
            } else {
                null
            }
        }
    }

    fun updateWorkout(workout: Workout): Int {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, workout.name)
            put(COLUMN_CATEGORY, workout.category)
            put(COLUMN_DURATION_MINUTES, workout.durationMinutes)
            put(COLUMN_EXERCISES, workout.exercises)
            put(COLUMN_NOTES, workout.notes)
            put(COLUMN_CREATED_AT, workout.createdAt)
        }

        return db.update(
            TABLE_WORKOUTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(workout.id.toString())
        )
    }

    fun deleteWorkout(id: Long): Int {
        val db = writableDatabase

        return db.delete(
            TABLE_WORKOUTS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    private fun cursorToWorkout(cursor: Cursor): Workout {
        return Workout(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
            category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
            durationMinutes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION_MINUTES)),
            exercises = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISES)),
            notes = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES)),
            createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT))
        )
    }

    companion object {
        private const val DATABASE_NAME = "fitplan_database.db"
        private const val DATABASE_VERSION = 2

        private const val TABLE_WORKOUTS = "workouts"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_DURATION_MINUTES = "duration_minutes"
        private const val COLUMN_EXERCISES = "exercises"
        private const val COLUMN_NOTES = "notes"
        private const val COLUMN_CREATED_AT = "created_at"
    }
}