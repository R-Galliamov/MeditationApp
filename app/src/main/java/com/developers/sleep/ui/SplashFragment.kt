package com.developers.sleep.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developers.sleep.viewModel.GeneralViewModel
import com.developers.sleep.PrefsConstants
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {


    private var _binding: FragmentSplashBinding? = null
    private val binding: FragmentSplashBinding
        get() = _binding!!

    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        @Volatile
        private var instance: SplashFragment? = null

        fun getInstance(): SplashFragment {
            return instance ?: synchronized(this) {
                instance ?: SplashFragment().also { instance = it }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(PrefsConstants.PREFS_GENERAL_NAME, Context.MODE_PRIVATE)

        val isFirstLaunch = sharedPreferences.getBoolean(PrefsConstants.IS_FIRST_LAUNCH, true)
        val isGoalChosen = sharedPreferences.getBoolean(PrefsConstants.IS_GOAL_CHOSEN, false)

        if (isFirstLaunch) {
            findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
        } else if (!isGoalChosen) {
            findNavController().navigate(R.id.action_splashFragment_to_choosingGoalFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
