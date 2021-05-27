package edu.ktu.fitnessapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import edu.ktu.fitnessapp.databinding.FragmentFinishBinding


class FinishFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binder = FragmentFinishBinding.inflate(inflater, container,  false)

        binder.backToMainMenuBtn.setOnClickListener {
            val action = FinishFragmentDirections.actionFinishFragmentToMainFragment()
            binder.root.findNavController().navigate(action)
        }

        return binder.root
    }


}