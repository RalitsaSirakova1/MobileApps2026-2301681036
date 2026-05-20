package com.example.fitplanproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class WorkoutDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fitplan.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_WORKOUTS = "workouts"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EXERCISE = "exercise"
        private const val COLUMN_DURATION = "duration"
        private const val COLUMN_CALORIES = "calories"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_WORKOUTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_EXERCISE TEXT NOT NULL,
                $COLUMN_DURATION INTEGER NOT NULL,
                $COLUMN_CALORIES INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKOUTS")
        onCreate(db)
    }

    fun addWorkout(workout: Workout): Boolean {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, workout.name)
            put(COLUMN_EXERCISE, workout.exercise)
            put(COLUMN_DURATION, workout.duration)
            put(COLUMN_CALORIES, workout.calories)
        }

        val result = db.insert(TABLE_WORKOUTS, null, values)
        db.close()

        return result != -1L
    }

    fun getAllWorkouts(): List<Workout> {
        val workouts = mutableListOf<Workout>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_WORKOUTS ORDER BY $COLUMN_ID DESC",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val workout = Workout(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    exercise = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE)),
                    duration = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                    calories = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIES))
                )

                workouts.add(workout)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return workouts
    }

    fun getWorkoutById(id: Int): Workout? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_WORKOUTS WHERE $COLUMN_ID = ?",
            arrayOf(id.toString())
        )

        var workout: Workout? = null

        if (cursor.moveToFirst()) {
            workout = Workout(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                exercise = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE)),
                duration = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                calories = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIES))
            )
        }

        cursor.close()
        db.close()

        return workout
    }

    fun updateWorkout(workout: Workout): Boolean {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, workout.name)
            put(COLUMN_EXERCISE, workout.exercise)
            put(COLUMN_DURATION, workout.duration)
            put(COLUMN_CALORIES, workout.calories)
        }

        val result = db.update(
            TABLE_WORKOUTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(workout.id.toString())
        )

        db.close()

        return result > 0
    }

    fun deleteWorkout(id: Int): Boolean {
        val db = writableDatabase

        val result = db.delete(
            TABLE_WORKOUTS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )

        db.close()

        return result > 0
    }
}