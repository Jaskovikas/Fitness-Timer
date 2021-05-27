package edu.ktu.fitnessapp.entities

import androidx.room.Embedded
import androidx.room.Relation

data class SetWithExercises (
    @Embedded val set : DatabaseSet,
    @Relation(
        parentColumn = "setId",
        entityColumn = "exerciseId"
    )
    val exercises : MutableList<DatabaseExercise>
)