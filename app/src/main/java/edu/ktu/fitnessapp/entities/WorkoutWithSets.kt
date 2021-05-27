package edu.ktu.fitnessapp.entities

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithSets (
    @Embedded val workout : Workout,
    @Relation(
            parentColumn = "workoutId",
            entityColumn = "setWorkoutId"
    )
    val sets : MutableList<DatabaseSet>
)
