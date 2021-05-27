package edu.ktu.fitnessapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.ktu.fitnessapp.databinding.ItemSetBinding


class SetAdapter(viewModel: SetViewModel) : ListAdapter<Set, SetAdapter.ViewHolder>(SetDiffCallBack()) {

    private val viewModel = viewModel

    class ViewHolder(val binding: ItemSetBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(set: Set, viewModel: SetViewModel) {
            binding.set = set
            binding.viewmodel = viewModel
            binding.pos = adapterPosition
        }
    }

    class SetDiffCallBack : DiffUtil.ItemCallback<Set>() {
        override fun areItemsTheSame(oldItem: Set, newItem: Set): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Set, newItem: Set): Boolean {
            return oldItem == newItem
        }

    }

//    override fun submitList(list: List<Set>?) {
//        super.submitList(list?.let { ArrayList(it) })
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSetBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position), viewModel)
    }
}