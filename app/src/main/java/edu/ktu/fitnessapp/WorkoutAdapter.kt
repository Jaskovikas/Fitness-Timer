package edu.ktu.fitnessapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.ktu.fitnessapp.entities.Workout

import edu.ktu.fitnessapp.databinding.ItemWorkoutBinding

class WorkoutAdapter(viewModel: WorkoutViewModel) : ListAdapter<Workout, WorkoutAdapter.ViewHolder>(SetDiffCallBack()) {

    private val viewModel = viewModel

    class ViewHolder(val binding: ItemWorkoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(workout: Workout, viewModel: WorkoutViewModel) {
            binding.workout = workout
            binding.viewmodel = viewModel
            binding.timeFormatted = formatTime(workout.totalTimeSeconds)
        }

        fun formatTime(sec : Long) : String {
            val minutesRemaining = sec / 60
            val secondsRemaining = sec % 60
            val minutes = appendZero(minutesRemaining)
            val seconds = appendZero(secondsRemaining)
            val timeText = "${minutes} : ${seconds}"
            return timeText


        }
        fun appendZero(number: Long): String {
            return String.format("%02d", number)
        }
    }

    class SetDiffCallBack : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }

    }

//    override fun submitList(list: List<Set>?) {
//        super.submitList(list?.let { ArrayList(it) })
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position), viewModel)
    }
}