package com.developers.sleep.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.apphud.sdk.Apphud
import com.developers.sleep.GeneralPrefs
import com.developers.sleep.R
import com.developers.sleep.UserDataPrefs
import com.developers.sleep.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

//TODO add changing after payment
var userIsPremium = false

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding: FragmentSplashBinding
        get() = _binding!!

    private lateinit var generalSharedPreferences: SharedPreferences
    private lateinit var userDataSharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generalSharedPreferences = requireActivity().getSharedPreferences(
            GeneralPrefs.PREFS_NAME,
            Context.MODE_PRIVATE
        )
        userDataSharedPreferences = requireActivity().getSharedPreferences(
            UserDataPrefs.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val isFirstLaunch =
            generalSharedPreferences.getBoolean(GeneralPrefs.IS_FIRST_LAUNCH, true)
        val isGoalChosen = generalSharedPreferences.getBoolean(GeneralPrefs.IS_GOAL_CHOSEN, false)

        userIsPremium = Apphud.hasPremiumAccess()

        if (isFirstLaunch) {
            findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
        } else if (!isGoalChosen) {
            findNavController().navigate(R.id.action_splashFragment_to_choosingGoalFragment)
        } else {
            if (userIsPremium) {
                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            } else {
                val navController = findNavController()
                val navOptions =
                    NavOptions.Builder().setPopUpTo(navController.currentDestination?.id ?: 0, true)
                        .build()
                navController.navigate(
                    R.id.action_splashFragment_to_paywallFragment,
                    null,
                    navOptions
                )
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
