package com.developers.sleep.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.developers.sleep.PrefsConstants
import com.developers.sleep.R
import com.developers.sleep.dataModels.QUESTION_LIST_TEST
import com.developers.sleep.databinding.FragmentHomeBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    companion object {
        @Volatile
        private var instance: HomeFragment? = null

        fun getInstance(): HomeFragment {
            return instance ?: synchronized(this) {
                instance ?: HomeFragment().also { instance = it }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences(
            PrefsConstants.TEST_DATA_PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val userAnswersCount = sharedPreferences.getInt(PrefsConstants.USER_ANSWERS_COUNT, 0)
        val userProgress = userAnswersCount.toFloat()
        val questionList = QUESTION_LIST_TEST
        val maxUserProgress = questionList.size.toFloat()
        val nextQuestion =
            if (userAnswersCount < questionList.size) questionList[userAnswersCount].question
            else getString(R.string.test_completed) //TODO replace

        if (userProgress == maxUserProgress) {
            binding.checkbox.visibility = View.VISIBLE
        }

        binding.nextQuestionText.text = nextQuestion
        val angle = (-45f * userProgress)
        val circularProgressBar = binding.circularProgressBar
        circularProgressBar.apply {
            progress = userProgress
            progressMax = maxUserProgress
            startAngle = angle
            setProgressWithAnimation(userProgress, 1000)
        }


        binding.buttonGoToTest.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_testFragment)
        }

        binding.buttonFallIntoADream.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_sleepSettingsFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
