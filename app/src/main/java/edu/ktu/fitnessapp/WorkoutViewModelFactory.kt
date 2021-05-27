package edu.ktu.fitnessapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WorkoutViewModelFactory(private val db: WorkoutDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            return WorkoutViewModel(db) as T
        }
        throw IllegalArgumentException()
    }
}