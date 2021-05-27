package edu.ktu.fitnessapp.BindingAdapters

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ktu.fitnessapp.Exercise
import edu.ktu.fitnessapp.ExerciseAdapter
import edu.ktu.fitnessapp.SetViewModel

@BindingAdapter("setExercises", "setViewModel", "setSetNumber")
fun RecyclerView.setExercises(exercises : List<Exercise>?,
                              viewModel: SetViewModel,
                              setNumber: Int) {
    if (exercises != null) {
        val exerciseAdapter = ExerciseAdapter(viewModel, setNumber)
        exerciseAdapter.submitList(exercises)
        layoutManager = LinearLayoutManager(context)
        adapter = exerciseAdapter
    }
}

