package com.developers.sleep.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.icu.text.DateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.developers.sleep.R
import com.developers.sleep.TestPrefs
import com.developers.sleep.UserDataPrefs
import com.developers.sleep.databinding.FragmentMyAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MyAccountFragment : Fragment() {
    private var _binding: FragmentMyAccountBinding? = null
    private val binding: FragmentMyAccountBinding
        get() = _binding!!

    private lateinit var userDataSP: SharedPreferences
    private var userName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDataSP =
            requireActivity().getSharedPreferences(UserDataPrefs.PREFS_NAME, Context.MODE_PRIVATE)

        val birthDate = userDataSP.getString(UserDataPrefs.USER_DATE, null)
        userName = userDataSP.getString(UserDataPrefs.USER_NAME, null)
        val gender = userDataSP.getString(UserDataPrefs.USER_GENDER, null)

        with(binding) {
            if (userName != null) {
                editName.setText(userName)
            }
            if (birthDate != null) {
                dateText.setTextColor(Color.WHITE)
                dateText.text = birthDate
            }
            if (gender != null) {
                editGender.setTextColor(Color.WHITE)
                editGender.text = gender
            } else {
                editGender.text = getString(R.string.gender)
            }

            editGender.setOnClickListener {
                genderChoosing.visibility = View.VISIBLE
            }

            buttonStatus.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_paywallFragment)
            }

            statusText.text = if (userIsPremium) "Premium" else "Basic"

            screenOverlaying.setOnClickListener {
                genderChoosing.visibility = View.GONE
                if (getSelectedAnswer() != "Gender") {
                    saveGender(getSelectedAnswer())
                    editGender.text = getSelectedAnswer()
                    editGender.setTextColor(Color.WHITE)
                }
            }

            buttonBack.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            buttonSelectDate.setOnClickListener {
                showDatePickerDialog()
            }
        }
    }

    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            AlertDialog.THEME_HOLO_DARK,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val formattedDate = formatDate(selectedDate.time)
                binding.dateText.text = formattedDate
                binding.dateText.setTextColor(Color.WHITE)

                val editor = userDataSP.edit()
                editor.putString(UserDataPrefs.USER_DATE, formattedDate)
                editor.apply()

            },
            currentYear,
            currentMonth,
            currentDayOfMonth
        )
        datePickerDialog.setTitle(R.string.date_of_birth)
        datePickerDialog.show()
    }

    private fun formatDate(date: Date): String {
        val dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun saveName(userName: String?) {
        if (!userName.isNullOrBlank()) {
            val editor = userDataSP.edit()
            editor.putString(UserDataPrefs.USER_NAME, userName)
            editor.apply()
        }
    }

    private fun saveGender(selectedGender: String) {
        val editor = userDataSP.edit()
        editor.putString(UserDataPrefs.USER_GENDER, selectedGender)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        saveName(binding.editName.text.toString())
    }

    private fun getSelectedAnswer(): String {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.answer0 -> "Male"
            R.id.answer1 -> "Female"
            R.id.answer2 -> "Other"
            else -> "Gender"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
