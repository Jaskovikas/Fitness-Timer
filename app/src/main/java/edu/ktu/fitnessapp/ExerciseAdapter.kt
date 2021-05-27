package edu.ktu.fitnessapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.ktu.fitnessapp.databinding.ItemExerciseBinding
import edu.ktu.fitnessapp.databinding.ItemSetBinding

class ExerciseAdapter(viewModel: SetViewModel, setNumber: Int) : ListAdapter<Exercise, ExerciseAdapter.ViewHolder>(ExerciseDiffCallBack()) {

    private val viewModel = viewModel
    private val setNumber = setNumber

    class ViewHolder(val binding: ItemExerciseBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(exercise: Exercise, viewModel: SetViewModel, setNumber: Int) {
            binding.exercise = exercise
            binding.viewmodel = viewModel
            binding.pos = adapterPosition
            binding.setPos = setNumber
        }
    }

    class ExerciseDiffCallBack : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position), viewModel, setNumber)
    }
}