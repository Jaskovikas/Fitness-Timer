package edu.ktu.fitnessapp

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ktu.fitnessapp.databinding.FragmentMainBinding
import edu.ktu.fitnessapp.entities.Workout
import kotlinx.coroutines.launch

class WorkoutViewModel(private val db : WorkoutDatabase) : ViewModel() {

    private val _workoutId = MutableLiveData<Long>()
    val workoutId: LiveData<Long>
        get() = _workoutId

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts : LiveData<List<Workout>>
        get() = _workouts

    init {
        getAllWorkouts()
    }

    fun getAllWorkouts() {
        viewModelScope.launch {
            _workouts.postValue(db.workoutDAO().getAllWorkouts())
        }
    }

    fun doRoutine( id : Long) {
        _workoutId.value = id
    }

    fun deleteRoutine(id : Long) {
        viewModelScope.launch {
            db.workoutDAO().deleteWorkout(id)
            getAllWorkouts()
        }
    }

}