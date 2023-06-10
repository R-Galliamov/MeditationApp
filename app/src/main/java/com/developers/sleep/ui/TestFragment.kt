package com.developers.sleep.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.developers.sleep.R
import com.developers.sleep.TestPrefs
import com.developers.sleep.dataModels.QUESTION_LIST_TEST
import com.developers.sleep.databinding.FragmentTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestFragment : Fragment() {
    private var _binding: FragmentTestBinding? = null
    private val binding: FragmentTestBinding
        get() = _binding!!

    private val questionList = QUESTION_LIST_TEST
    private lateinit var testSP: SharedPreferences
    private var userAnswersCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        testSP = requireActivity().getSharedPreferences(
            TestPrefs.PREFS_NAME,
            Context.MODE_PRIVATE
        )
        userAnswersCount = testSP.getInt(TestPrefs.USER_ANSWERS_COUNT, 0)
        if (userAnswersCount == questionList.size) {
            userAnswersCount--
        }
        updateQuestion(userAnswersCount)
        if (userAnswersCount == questionList.size - 1) {
            binding.continueText.text = getString(R.string.done)
        }

        with(binding) {
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                buttonChoose.visibility = if (checkedId != -1) View.VISIBLE else View.GONE
            }

            buttonChoose.setOnClickListener {
                userAnswersCount++
                saveAnswer(userAnswersCount, getSelectedAnswer())
                if (userAnswersCount == questionList.size - 1) {
                    continueText.text = getString(R.string.done)
                }
                if (userAnswersCount < questionList.size) {
                    updateQuestion(userAnswersCount)
                } else if (userAnswersCount == questionList.size) {
                    findNavController().navigateUp()
                }
            }

            buttonBack.setOnClickListener {
                findNavController().navigateUp()
            }

            buttonPreviousQuestion.setOnClickListener {
                if (userAnswersCount > 0) {
                    userAnswersCount--
                    updateQuestion(userAnswersCount)
                }
                if (userAnswersCount < questionList.size - 1) {
                    continueText.text = getString(R.string.continueText)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateQuestion(userAnswersCount: Int) {
        val currentQuestion = questionList[userAnswersCount]
        with(binding) {
            questionText.text = currentQuestion.question
            val currentQuestionNumber = userAnswersCount + 1
            val questionNumString =
                getString(R.string.question_number, currentQuestionNumber, questionList.size)
            questionNumText.text = questionNumString

            answer0.text = currentQuestion.answers[0]
            answer1.text = currentQuestion.answers[1]
            answer2.text = currentQuestion.answers[2]
            answer3.text = currentQuestion.answers[3]

            val previousAnswer =
                testSP.getInt(TestPrefs.ANSWER_PREFIX + userAnswersCount, -1)
            if (previousAnswer != -1) {
                buttonChoose.visibility = View.VISIBLE
                setRadioGroupSelection(previousAnswer)
            } else {
                buttonChoose.visibility = View.GONE
                radioGroup.clearCheck()
            }

            buttonPreviousQuestion.visibility =
                if (userAnswersCount == 0) View.GONE else View.VISIBLE
        }
    }

    private fun saveAnswer(userAnswersCount: Int, selectedAnswer: Int) {
        val editor = testSP.edit()
        editor.putInt(TestPrefs.USER_ANSWERS_COUNT, userAnswersCount)
        editor.putInt(TestPrefs.ANSWER_PREFIX + (userAnswersCount - 1), selectedAnswer)
        editor.apply()
    }

    private fun getSelectedAnswer(): Int {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.answer0 -> 0
            R.id.answer1 -> 1
            R.id.answer2 -> 2
            R.id.answer3 -> 3
            else -> -1
        }
    }

    private fun setRadioGroupSelection(selectedAnswer: Int) {
        when (selectedAnswer) {
            0 -> binding.radioGroup.check(R.id.answer0)
            1 -> binding.radioGroup.check(R.id.answer1)
            2 -> binding.radioGroup.check(R.id.answer2)
            3 -> binding.radioGroup.check(R.id.answer3)
            else -> binding.radioGroup.clearCheck()
        }
    }
}
