package edu.ktu.fitnessapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseExercise (
    @PrimaryKey(autoGenerate = true) val exerciseId : Long,
    var name: String,
    var timeText: String,
    var setId: Long
)
