package edu.ktu.fitnessapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.ktu.fitnessapp.entities.DatabaseExercise
import edu.ktu.fitnessapp.entities.DatabaseSet
import edu.ktu.fitnessapp.entities.Workout
import edu.ktu.fitnessapp.entities.WorkoutDAO

@Database(entities = [Workout::class, DatabaseSet::class, DatabaseExercise::class], version = 1)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDAO(): WorkoutDAO

    companion object {
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(context: Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WorkoutDatabase::class.java,
                        "workout_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}