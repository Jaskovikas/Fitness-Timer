package edu.ktu.fitnessapp.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import edu.ktu.fitnessapp.Exercise
import edu.ktu.fitnessapp.Set

@Dao
interface WorkoutDAO {

    @Insert
    suspend fun addNewSet(newSet : DatabaseSet) : Long

    @Insert
    suspend fun addNewExercises(newExercises: MutableList<DatabaseExercise>)

    @Insert
    suspend fun addNewWorkout(newWorkout : Workout) : Long

    @Insert
    suspend fun addNewSets(newSets : MutableList<DatabaseSet>)

    @Insert
    suspend fun addNewExercise(newExercise: DatabaseExercise)

    @Transaction
    suspend fun addWorkoutWithSetsAndExercises(workout: Workout, setsList : ArrayList<Set>) {

        val workoutId = addNewWorkout(workout)


        setsList.forEach {
            //add the set, save the id that was given
            val setId = addNewSet(DatabaseSet(0, it.number, it.count, workoutId))
            //add all exercises
            it.exercises.forEach {
                addNewExercise(DatabaseExercise(0, it.name, it.timeText, setId))
            }
        }


    }

    @Transaction
    suspend fun getWorkoutWithSetsAndExercises(workoutId: Long) : ArrayList<Set> {

        var sets = getWorkoutSets(workoutId)
        var setsToReturn = ArrayList<Set>()

        sets.forEach {
            var exercises = getSetExercises(it.setId)
            var listOfExercises = mutableListOf<Exercise>()

            exercises.forEach {
                listOfExercises.add(Exercise(it.name, it.timeText))
            }
            setsToReturn.add(Set(it.number, it.count, listOfExercises))
        }
        return setsToReturn
    }

    @Transaction
    suspend fun deleteWorkoutWithSetsAndExercises(workoutId: Long) {

        var sets = getWorkoutSets(workoutId)
        //for each workout set delete it along with its exercises
        sets.forEach {
            deleteExercisesForSet(it.setId)

        }
        deleteSetsForWorkout(workoutId)
        deleteWorkout(workoutId)
    }

    @Transaction
    @Query("DELETE FROM  Workout WHERE workoutId=:id")
    suspend fun deleteWorkout(id : Long)

    @Transaction
    @Query("DELETE FROM DatabaseSet WHERE setWorkoutId=:id")
    suspend fun deleteSetsForWorkout(id : Long)

    @Transaction
    @Query("DELETE FROM DatabaseExercise WHERE setId=:id")
    suspend fun deleteExercisesForSet(id: Long)

    @Transaction
    @Query("SELECT * FROM Workout WHERE workoutId=:id")
    suspend fun getWorkout(id: Long) : Workout

    @Transaction
    @Query("SELECT * FROM Workout")
    suspend fun getAllWorkouts() : List<Workout>

    @Transaction
    @Query("SELECT * FROM DatabaseSet WHERE setWorkoutId=:id")
    suspend fun getWorkoutSets(id: Long) : List<DatabaseSet>

    @Transaction
    @Query("SELECT * FROM DatabaseExercise WHERE setId=:id")
    suspend fun getSetExercises(id: Long) : List<DatabaseExercise>

    @Transaction
    @Query("SELECT * FROM Workout")
    suspend fun getAllWorkoutsWithSets() : MutableList<WorkoutWithSets>

    @Transaction
    @Query("SELECT * FROM Workout WHERE workoutId = :id")
    suspend fun getWorkoutWithSets(id: Int) : WorkoutWithSets

    @Transaction
    @Query("SELECT * FROM DatabaseSet")
    suspend fun getAllSetsWithExercises() : MutableList<SetWithExercises>
}