package com.developers.sleep.ui.onboarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.developers.sleep.GeneralPrefs
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentChoosingGoalBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChoosingGoalFragment : Fragment() {
    private var _binding: FragmentChoosingGoalBinding? = null
    private val binding: FragmentChoosingGoalBinding
        get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        @Volatile
        private var instance: ChoosingGoalFragment? = null

        fun getInstance(): ChoosingGoalFragment {
            return instance ?: synchronized(this) {
                instance ?: ChoosingGoalFragment().also { instance = it }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChoosingGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity().getSharedPreferences(GeneralPrefs.PREFS_NAME, Context.MODE_PRIVATE)

        binding.buttonBlueCard.setOnClickListener {
            findNavController().navigate(R.id.action_choosingGoalFragment_to_paywallFragment)
            setGoalIsChosen(true)
        }
        binding.buttonGreenCard.setOnClickListener {
            findNavController().navigate(R.id.action_choosingGoalFragment_to_paywallFragment)
            setGoalIsChosen(true)
        }
        binding.buttonCrimsonCard.setOnClickListener {
            findNavController().navigate(R.id.action_choosingGoalFragment_to_paywallFragment)
            setGoalIsChosen(true)
        }
        binding.buttonYellowCard.setOnClickListener {
            findNavController().navigate(R.id.action_choosingGoalFragment_to_paywallFragment)
            setGoalIsChosen(true)
        }
    }

    private fun setGoalIsChosen(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(GeneralPrefs.IS_GOAL_CHOSEN, value)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
