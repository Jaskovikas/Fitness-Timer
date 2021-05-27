package edu.ktu.fitnessapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import edu.ktu.fitnessapp.databinding.FragmentMainBinding


class MainFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val viewModel : WorkoutViewModel by viewModels {
            WorkoutViewModelFactory(
                    WorkoutDatabase.getDatabase(
                            requireContext()
                    )
            )
        }

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        val adapter = WorkoutAdapter(viewModel)
        binding.workoutsList.adapter = adapter

        viewModel.workouts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.workoutId.observe(viewLifecycleOwner) {
            val action = MainFragmentDirections
                .actionMainFragmentToWorkoutFragment(it)
            binding.root.findNavController().navigate(action)
        }

        binding.addWorkoutButton
                .setOnClickListener {
                    val action = MainFragmentDirections
                            .actionMainFragmentToWorkoutFragment(-1)
                    binding.root.findNavController()
                            .navigate(action)
                }
        return binding.root
    }


}