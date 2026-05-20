package com.example.fitplanproject

data class Workout(
    val id: Int = 0,
    val name: String,
    val exercise: String,
    val duration: Int,
    val calories: Int
)