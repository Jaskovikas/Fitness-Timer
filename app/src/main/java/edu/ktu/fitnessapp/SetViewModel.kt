package edu.ktu.fitnessapp

import android.text.Editable
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ktu.fitnessapp.entities.Workout
import kotlinx.coroutines.launch

class SetViewModel(private val db : WorkoutDatabase) : ViewModel() {

    private var setsList = ArrayList<Set>()
    private val _setsLiveData = MutableLiveData<List<Set>>()
    val sets : LiveData<List<Set>>
        get() = _setsLiveData

    private var _routineName = MutableLiveData<String>()
    val routineName : LiveData<String>
        get() = _routineName

    init {
        createDefaultSet()
        getAllSetsForWorkout()
    }

    fun getAllSetsForWorkout() {
        viewModelScope.launch {

            _setsLiveData.postValue(setsList)

        }
    }

    fun saveSetsToDb(workoutName: String) {

        //calculate total time
        val totalTime = totalSetsTimeSeconds()


        //do transaction in async
        val workout = Workout(0, workoutName, totalTime)

        viewModelScope.launch {
            db.workoutDAO().addWorkoutWithSetsAndExercises(workout, setsList)

        }
    }

    fun totalSetsTimeSeconds(): Long {
        var total : Long = 0
        setsList.forEach {
            var setTotal : Long = 0
            it.exercises.forEach {
                setTotal += calculateTimeInSeconds(it.timeText)
            }
            total += setTotal * it.count
        }
        return total
    }

    fun loadFromDb(workoutId: Long) {
        viewModelScope.launch {
            setsList.clear()
            getRoutineName(workoutId)
            setsList = db.workoutDAO().getWorkoutWithSetsAndExercises(workoutId)
            _setsLiveData.postValue(setsList)
        }
    }

    fun getRoutineName(workoutId: Long) {
        viewModelScope.launch {
            _routineName.value = db.workoutDAO().getWorkout(workoutId).name
        }
    }

    private fun calculateTimeInSeconds(text : String): Long {
        val exerciseTime = text
        val minAndSecList = exerciseTime.split(":").toTypedArray()
        val timeInSeconds = minAndSecList[0].toLong() * 60 + minAndSecList[1].toLong()
        return timeInSeconds
    }

    fun createDefaultSet() {
        val exercise = Exercise("Work",  "0:30")
        val rest = Exercise("Rest", "0:30")
        val exercises = arrayListOf(exercise, rest)

        setsList = arrayListOf(Set(1, 1,exercises))
    }

    fun createNewSet() {
        val exercise = Exercise("Work",  "0:30")
        val rest = Exercise("Rest", "0:30")
        val exercises = arrayListOf(exercise, rest)
        val set = Set(setsList.size + 1, 1, exercises)
        setsList.add(set)
        getAllSetsForWorkout()
        //Log.d("Sets array",  _setsLiveData.value.toString())

    }

    fun addNewExercise(setNumber : Int) {
        val exercise = Exercise("Work", "0:30")
        //Log.d("Went there", "Added Successfully")
        viewModelScope.launch {
            setsList[setNumber-1].exercises.add(exercise)
            _setsLiveData.postValue(setsList)
        }

    }

    fun updateLiveData() {
        viewModelScope.launch {
            _setsLiveData.postValue(setsList)
            //Log.d("Sets array after update",  _setsLiveData.value.toString())
        }
    }

    fun onExerciseNameChanged(text: Editable, position: Int, setPosition: Int) {
//       Log.d("SetPos", setPosition.toString())
//       Log.d("Name", position.toString())

        setsList[setPosition].exercises[position].name = text.toString()


    }

    fun onExerciseTimeChanged(text: Editable, position: Int, setPosition: Int) {
//        Log.d("SetPos", setPosition.toString())
//        Log.d("Time", position.toString())
        setsList[setPosition].exercises[position].timeText = text.toString()

    }

    fun deleteExercise(pos: Int, setPos: Int) {
        setsList[setPos].exercises.removeAt(pos)
        updateLiveData()
    }

    fun subtractSetCount(setPos: Int) {
        if (setsList[setPos].count > 1) {
            setsList[setPos].count--
            updateLiveData()
        }
    }
    fun addtSetCount(setPos: Int) {
        setsList[setPos].count++
        updateLiveData()
    }

    fun deleteSet(setPos: Int){
        setsList.removeAt(setPos)
        updateLiveData()
    }
}