package com.developers.sleep.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentMyAccountBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MyAccountFragment : Fragment() {
    private var _binding: FragmentMyAccountBinding? = null
    private val binding: FragmentMyAccountBinding
        get() = _binding!!

    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonBack.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            buttonSelectDate.setOnClickListener {
                showDatePickerDialog()
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            AlertDialog.THEME_HOLO_DARK,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val formattedDate = formatDate(selectedDate.time)
                binding.dateText.text = formattedDate
                binding.dateText.setTextColor(Color.WHITE)
            },
            2000,
            1,
            1
        )
        datePickerDialog.setTitle(R.string.date_of_birth)
        datePickerDialog.show()
    }

    private fun formatDate(date: Date): String {
        val dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
        return dateFormat.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
