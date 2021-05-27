package edu.ktu.fitnessapp


data class Set (
    val number: Int,
    var count: Int,
    val exercises: MutableList<Exercise>
)