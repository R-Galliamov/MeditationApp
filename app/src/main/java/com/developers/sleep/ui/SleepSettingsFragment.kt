package com.developers.sleep.ui

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.developers.sleep.PrefsConstants
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentSleepSettingsBinding

import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@AndroidEntryPoint
class SleepSettingsFragment : Fragment() {
    private var _binding: FragmentSleepSettingsBinding? = null
    private val binding: FragmentSleepSettingsBinding
        get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        @Volatile
        private var instance: SleepSettingsFragment? = null

        fun getInstance(): SleepSettingsFragment {
            return instance ?: synchronized(this) {
                instance ?: SleepSettingsFragment().also { instance = it }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSleepSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(
            PrefsConstants.PREFS_GENERAL_NAME,
            Context.MODE_PRIVATE
        )


        binding.alarmTime.setOnClickListener {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = format.parse(binding.alarmTime.text.toString())
            val calendar = Calendar.getInstance()
            calendar.time = date
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            //TODO make custom dialog or change buttons color
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    binding.alarmTime.text = selectedTime

                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }

        with(binding) {

            alarmSoundChooseButton.text =
                sharedPreferences.getString(
                    PrefsConstants.SELECTED_ALARM_MELODY_NAME,
                    "Sound 1"
                ) //TODO refactor with viewModel


            alarmSoundChooseButton.setOnClickListener {
                findNavController().navigate(R.id.action_sleepSettingsFragment_to_choosingAlarmFragment)
            }

            buttonBack.setOnClickListener {
                findNavController().navigateUp()
            }

            switcher.isChecked =
                sharedPreferences.getBoolean(PrefsConstants.IS_MUSIC_FOR_SLEEP_0N, true)
            updateDiveIntoSleepCardUi(switcher.isChecked)
            saveSwitcherState(switcher.isChecked)
            switcher.setOnCheckedChangeListener { _, isChecked ->
                updateDiveIntoSleepCardUi(isChecked)
                saveSwitcherState(isChecked)
            }
        }

    }

    private fun updateDiveIntoSleepCardUi(isChecked: Boolean) = with(binding) {
        overlaying.visibility = if (isChecked) View.GONE else View.VISIBLE
        buttonDiveIntoSleep.isEnabled = isChecked
    }

    private fun saveSwitcherState(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PrefsConstants.IS_MUSIC_FOR_SLEEP_0N, value)
        editor.apply()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
