package com.developers.sleep.ui

import android.app.AlertDialog
import android.app.AlertDialog.THEME_HOLO_DARK
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developers.sleep.AlarmPrefs
import com.developers.sleep.AlarmPlayerPrefs
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentSleepSettingsBinding
import com.developers.sleep.viewModel.AlarmViewModel
import com.developers.sleep.viewModel.AlarmPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SleepSettingsFragment : Fragment() {
    private var _binding: FragmentSleepSettingsBinding? = null
    private val binding: FragmentSleepSettingsBinding
        get() = _binding!!

    private lateinit var alarmSharedPreferences: SharedPreferences
    private lateinit var playerSharedPreferences: SharedPreferences

    private var notificationsDialog: AlertDialog? = null

    private val alarmViewModel: AlarmViewModel by activityViewModels()
    private val alarmPlayerViewModel: AlarmPlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSleepSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!areNotificationsEnabled()) {
            showNotificationDialog()
        }
        alarmSharedPreferences = requireActivity().getSharedPreferences(
            AlarmPrefs.PREFS_NAME,
            Context.MODE_PRIVATE
        )
        playerSharedPreferences =
            requireActivity().getSharedPreferences(
                AlarmPlayerPrefs.PREFS_NAME,
                Context.MODE_PRIVATE
            )

        binding.alarmTime.setOnClickListener {
            val previousCalendar = alarmViewModel.alarmTime.value
            val previousHour = previousCalendar?.get(Calendar.HOUR_OF_DAY)
            val previousMinute = previousCalendar?.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                THEME_HOLO_DARK,
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
                alarmSharedPreferences.getBoolean(AlarmPrefs.IS_MUSIC_FOR_SLEEP_0N, true)
            updateMusicForSleepCardUi(switcher.isChecked)

            saveSwitcherState(switcher.isChecked)

            switcher.setOnCheckedChangeListener { _, isChecked ->
                updateMusicForSleepCardUi(isChecked)
                saveSwitcherState(isChecked)
            }

            buttonSongChooser.setOnClickListener {
                findNavController().navigate(R.id.action_sleepSettingsFragment_to_melodyChooserFragment)
            }

            buttonSleepingTime.setOnClickListener {
                showNumberPicker()
            }


            //TODO replace with viewmodel
            var musicDuration = playerSharedPreferences.getInt(AlarmPlayerPrefs.MUSIC_DURATION, 30)

            numberPicker.apply {
                minValue = 1
                maxValue = 59
                value = musicDuration
            }

            sleepingTimeText.text = getString(R.string.min, musicDuration)

            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                musicDuration = newVal
            }

            screenOverlaying.setOnClickListener {
                numberPickerContainer.visibility = View.GONE
                sleepingTimeText.text = getString(R.string.min, musicDuration)
                alarmPlayerViewModel.setMusicDurationInMinutes(musicDuration)
            }

            alarmPlayerViewModel.currentMelody.observe(viewLifecycleOwner){
                songNameText.text = it.name
            }
            alarmPlayerViewModel.currentPlaylist.observe(viewLifecycleOwner){
                playlistNameText.text = it.name
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

    private fun updateMusicForSleepCardUi(isChecked: Boolean) = with(binding) {
        overlaying.visibility = if (isChecked) View.GONE else View.VISIBLE
        buttonSongChooser.isEnabled = isChecked
        buttonSleepingTime.isEnabled = isChecked
    }

    private fun saveSwitcherState(value: Boolean) {
        val editor = alarmSharedPreferences.edit()
        editor.putBoolean(AlarmPrefs.IS_MUSIC_FOR_SLEEP_0N, value)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        notificationsDialog?.hide()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun areNotificationsEnabled(): Boolean {
        val notificationManager = NotificationManagerCompat.from(requireContext())
        return notificationManager.areNotificationsEnabled()
    }

    private fun showNotificationDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_notifications, null)
        val allowButton = dialogView.findViewById<FrameLayout>(R.id.buttonAllow)
        allowButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            startActivity(intent)
        }
        val builder = AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
        builder.setView(dialogView)
        builder.setCancelable(true)
        notificationsDialog = builder.create()
        val notNowButton = dialogView.findViewById<FrameLayout>(R.id.buttonNotNow)
        notNowButton.setOnClickListener { notificationsDialog?.dismiss() }
        notificationsDialog?.show()
    }

}
