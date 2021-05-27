package edu.ktu.fitnessapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseSet (
    @PrimaryKey(autoGenerate = true) var setId : Long,
    var number: Int,
    var count: Int,
    var setWorkoutId: Long
)