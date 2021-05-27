package edu.ktu.fitnessapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ktu.fitnessapp.databinding.FragmentWorkoutBinding


class WorkoutFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binder = FragmentWorkoutBinding.inflate(inflater, container, false)

        val viewModel : SetViewModel by activityViewModels {
            SetViewModelFactory(
                    WorkoutDatabase.getDatabase(
                            requireContext()
                    )
            )
        }

        binder.viewmodel = viewModel
        binder.lifecycleOwner = this



        val adapter = SetAdapter(viewModel)
        binder.setsList.adapter = adapter

        val args : WorkoutFragmentArgs by navArgs()
        if (args.workoutId >= 0) {
            //load workout from db
            viewModel.loadFromDb(args.workoutId)
        } else {
            binder.workoutName = "New Routine"
            viewModel.createDefaultSet()
            viewModel.getAllSetsForWorkout()
        }

        viewModel.routineName.observe(viewLifecycleOwner) {
            binder.workoutName = it
        }

        //crashas del poziciju be sito???????
        //reik sutvarkyt
        //binder.setsList.itemAnimator = null

        viewModel.sets.observe(viewLifecycleOwner) {

            adapter.notifyDataSetChanged()
            adapter.submitList(null)
            adapter.submitList(it)
        }

        binder.backFromWorkoutBtn.setOnClickListener {
            val action = WorkoutFragmentDirections.actionWorkoutFragmentToMainFragment()
            binder.root.findNavController().navigate(action)
        }

        binder.toolbar.setOnMenuItemClickListener{ menuItem ->
            when(menuItem.itemId) {
                R.id.start_routine -> {
                    viewModel.updateLiveData()
                    val action = WorkoutFragmentDirections
                            .actionWorkoutFragmentToSessionFragment()
                    binder.root.findNavController()
                            .navigate(action)
                    true
                }
                R.id.save_routine -> {
                    showDialog(viewModel)
                    true
                }
                else -> false
            }

        }

        return binder.root
    }

    fun showDialog(viewModel: SetViewModel){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Enter Your New Workout Name:")

        // Set up the input
        val input = EditText(context)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Workout Name")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            var m_Text = input.text.toString()
            if (m_Text.isNotEmpty()) {
                viewModel.saveSetsToDb(m_Text)
                Toast.makeText(context, "Workout Saved!", Toast.LENGTH_SHORT).show()
            }

        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }



}