package com.example.fitplan2301681036.data.model

data class Workout(
    val id: Long = 0,
    val name: String,
    val category: String,
    val durationMinutes: Int,
    val exercises: String,
    val notes: String,
    val createdAt: Long = System.currentTimeMillis()
)