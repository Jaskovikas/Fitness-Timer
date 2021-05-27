package edu.ktu.fitnessapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SetViewModelFactory(private val db: WorkoutDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SetViewModel::class.java)) {
            return SetViewModel(db) as T
        }
        throw IllegalArgumentException()
    }
}