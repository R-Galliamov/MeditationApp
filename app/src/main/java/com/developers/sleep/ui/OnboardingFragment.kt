package com.developers.sleep.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.developers.sleep.PrefsConstants
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentOnboardingBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding: FragmentOnboardingBinding
        get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        @Volatile
        private var instance: OnboardingFragment? = null

        fun getInstance(): OnboardingFragment {
            return instance ?: synchronized(this) {
                instance ?: OnboardingFragment().also { instance = it }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var screenPosition = 0

        sharedPreferences =
            requireActivity().getSharedPreferences(PrefsConstants.PREFS_GENERAL_NAME, Context.MODE_PRIVATE)

        with(binding) {
            image.setImageResource(R.drawable.worky_yoga_woman)
            titleText.setText(R.string.firstWelcomeTitle)
            descriptionText.setText(R.string.firstWelcomeText)

            binding.buttonNext.setOnClickListener {
                when (screenPosition) {
                    0 -> {
                        screenPosition++
                        with(binding) {
                            image.setImageResource(R.drawable.worky_reading_woman)
                            titleText.setText(R.string.secondWelcomeTitle)
                            descriptionText.setText(R.string.secondWelcomeText)
                        }
                    }

                    1 -> {
                        screenPosition++
                        with(binding) {
                            image.setImageResource(R.drawable.worky_smartphone)
                            titleText.setText(R.string.thirdWelcomeTitle)
                            descriptionText.setText(R.string.thirdWelcomeText)
                            buttonText.setText(R.string.start)
                        }
                    }

                    2 -> {
                        setFirstLaunch(false)
                        findNavController().navigate(R.id.action_onboardingFragment_to_choosingGoalFragment)
                    }
                }
                updateIndicator(screenPosition)
            }
        }
    }

    private fun updateIndicator(screenPosition: Int) {
        val activeDrawable = R.drawable.blue_circle_indicator
        val inactiveDrawable = R.drawable.gray_circle_indicator

        with(binding) {
            when (screenPosition) {
                0 -> {
                    indicator1.setBackgroundResource(activeDrawable)
                    indicator2.setBackgroundResource(inactiveDrawable)
                    indicator3.setBackgroundResource(inactiveDrawable)

                    indicator1.layoutParams.height =
                        resources.getDimensionPixelSize(R.dimen.active_indicator_size)
                    indicator2.layoutParams.height =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)
                    indicator3.layoutParams.height =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)

                    indicator1.layoutParams.width =
                        resources.getDimensionPixelSize(R.dimen.active_indicator_size)
                    indicator2.layoutParams.width =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)
                    indicator3.layoutParams.width =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)
                }

                1 -> {
                    indicator1.setBackgroundResource(inactiveDrawable)
                    indicator2.setBackgroundResource(activeDrawable)
                    indicator3.setBackgroundResource(inactiveDrawable)

                    indicator1.layoutParams.height =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)
                    indicator2.layoutParams.height =
                        resources.getDimensionPixelSize(R.dimen.active_indicator_size)
                    indicator3.layoutParams.height =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)

                    indicator1.layoutParams.width =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)
                    indicator2.layoutParams.width =
                        resources.getDimensionPixelSize(R.dimen.active_indicator_size)
                    indicator3.layoutParams.width =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)
                }

                2 -> {
                    indicator1.setBackgroundResource(inactiveDrawable)
                    indicator2.setBackgroundResource(inactiveDrawable)
                    indicator3.setBackgroundResource(activeDrawable)

                    indicator1.layoutParams.height =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)
                    indicator2.layoutParams.height =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)
                    indicator3.layoutParams.height =
                        resources.getDimensionPixelSize(R.dimen.active_indicator_size)

                    indicator1.layoutParams.width =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)
                    indicator2.layoutParams.width =
                        resources.getDimensionPixelSize(R.dimen.inactive_indicator_size)
                    indicator3.layoutParams.width =
                        resources.getDimensionPixelSize(R.dimen.active_indicator_size)
                }
            }
        }
    }

    private fun setFirstLaunch(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PrefsConstants.IS_FIRST_LAUNCH, value)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
