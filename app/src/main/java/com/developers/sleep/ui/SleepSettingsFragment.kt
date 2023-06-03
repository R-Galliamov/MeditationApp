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
import androidx.fragment.app.activityViewModels
import com.developers.sleep.PrefsConstants
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentSleepSettingsBinding
import com.developers.sleep.viewModel.AlarmViewModel

import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SleepSettingsFragment : Fragment() {
    private var _binding: FragmentSleepSettingsBinding? = null
    private val binding: FragmentSleepSettingsBinding
        get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val alarmViewModel: AlarmViewModel by activityViewModels()

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
            val previousCalendar = alarmViewModel.alarmTime.value
            val previousHour = previousCalendar?.get(Calendar.HOUR_OF_DAY)
            val previousMinute = previousCalendar?.get(Calendar.MINUTE)

            //TODO make custom dialog or change background
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    val selectedTime = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                    }
                    alarmViewModel.setAlarmTime(selectedTime)
                },
                previousHour ?: 0,
                previousMinute ?: 0,
                true
            )
            timePickerDialog.show()
        }

        alarmViewModel.alarmTime.observe(viewLifecycleOwner) { calendar ->
            val alarmTimeFormatted = calendar?.let {
                val hour = it.get(Calendar.HOUR_OF_DAY)
                val minute = it.get(Calendar.MINUTE)
                String.format("%02d:%02d", hour, minute)
            } ?: ""

            binding.alarmTime.text = alarmTimeFormatted
        }

        alarmViewModel.chosenAlarmSound.observe(viewLifecycleOwner) {
            binding.alarmSoundChooseButton.text = it.name
        }

        with(binding) {
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

            buttonDiveIntoSleep.setOnClickListener {
                findNavController().navigate(R.id.action_sleepSettingsFragment_to_sleepDiverFragment)
            }

            buttonFallIntoADream.setOnClickListener {
                findNavController().navigate(R.id.action_sleepSettingsFragment_to_sleepDiverFragment)
                alarmViewModel.setAlarm()
            }
        }
    }

    private fun updateDiveIntoSleepCardUi(isChecked: Boolean) = with(binding) {
        overlaying.visibility = if (isChecked) View.GONE else View.VISIBLE
        buttonDiveIntoSleep.isEnabled = isChecked
    }

    private fun saveSwitcherState(value: Boolean) {
        //TODO replace to repository
        val editor = sharedPreferences.edit()
        editor.putBoolean(PrefsConstants.IS_MUSIC_FOR_SLEEP_0N, value)
        editor.apply()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
