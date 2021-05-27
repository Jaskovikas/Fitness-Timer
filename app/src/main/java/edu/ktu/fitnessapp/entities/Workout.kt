package edu.ktu.fitnessapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout (
    @PrimaryKey(autoGenerate = true) var workoutId : Long,
    val name: String,
    val totalTimeSeconds : Long
)