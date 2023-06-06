package com.developers.sleep.ui

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developers.sleep.AlarmPrefs
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
            AlarmPrefs.PREFS_NAME,
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
                sharedPreferences.getBoolean(AlarmPrefs.IS_MUSIC_FOR_SLEEP_0N, true)
            updateDiveIntoSleepCardUi(switcher.isChecked)
            saveSwitcherState(switcher.isChecked)
            switcher.setOnCheckedChangeListener { _, isChecked ->
                updateDiveIntoSleepCardUi(isChecked)
                saveSwitcherState(isChecked)
            }

            buttonSongChooser.setOnClickListener {
                findNavController().navigate(R.id.action_sleepSettingsFragment_to_melodyChooserFragment)
            }

            buttonSleepingTime.setOnClickListener {
                showNumberPicker()
            }

            val musicDuration = sharedPreferences.getInt(AlarmPrefs.MUSIC_DURATION, 30)

            numberPicker.apply {
                minValue = 1
                maxValue = 59
                value = musicDuration
            }

            sleepingTimeText.text = getString(R.string.min, musicDuration)

            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                sleepingTimeText.text = getString(R.string.min, newVal)
                saveMusicDuration(newVal)
            }

            screenOverlaying.setOnClickListener {
                numberPickerContainer.visibility = View.GONE
            }

            buttonFallIntoADream.setOnClickListener {
                findNavController().navigate(R.id.action_sleepSettingsFragment_to_sleepPlayerFragment)
                alarmViewModel.setAlarm()
            }
        }
    }

    private fun showNumberPicker() {
        binding.numberPickerContainer.visibility = View.VISIBLE
    }


    private fun updateDiveIntoSleepCardUi(isChecked: Boolean) = with(binding) {
        overlaying.visibility = if (isChecked) View.GONE else View.VISIBLE
        buttonSongChooser.isEnabled = isChecked
        buttonSleepingTime.isEnabled = isChecked
    }

    private fun saveMusicDuration(musicDuration: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(AlarmPrefs.IS_MUSIC_FOR_SLEEP_0N, musicDuration)
        editor.apply()
    }

    private fun saveSwitcherState(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(AlarmPrefs.IS_MUSIC_FOR_SLEEP_0N, value)
        editor.apply()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
