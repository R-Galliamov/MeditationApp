package com.developers.sleep.ui

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
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.service.MediaPlayerHelper
import com.developers.sleep.adapter.AlarmSoundAdapter
import com.developers.sleep.dataModels.AlarmSound
import com.developers.sleep.databinding.FragmentChoosingAlarmBinding
import com.developers.sleep.viewModel.AlarmViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChoosingAlarmFragment : Fragment() {
    private var _binding: FragmentChoosingAlarmBinding? = null
    private val binding: FragmentChoosingAlarmBinding
        get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private val alarmViewModel: AlarmViewModel by activityViewModels()

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChoosingAlarmBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(
            AlarmPrefs.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val adapter = AlarmSoundAdapter(object : AlarmSoundAdapter.OnAlarmSoundClickListener {
            override fun onMelodyClick(alarmSound: AlarmSound, position: Int) {
                mediaPlayerHelper.playAlarmSound(alarmSound.fileName)
                saveSelectedMelody(alarmSound, position)
            }
        }, getSelectedMelodyIndex() ?: 0)

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        val alarmSounds = alarmViewModel.getAlarmSoundsList()
        adapter.submitList(alarmSounds)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonChoose.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerHelper.stopPlaying()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSelectedMelodyIndex(): Int? {
        return sharedPreferences.getInt(AlarmPrefs.SELECTED_ALARM_MELODY_INDEX, -1)
            .takeIf { it >= 0 }
    }

    private fun saveSelectedMelody(alarmSound: AlarmSound, position: Int) {
        val selectedPosition = if (position >= 0) position else 0
        val editor = sharedPreferences.edit()
        editor.putInt(AlarmPrefs.SELECTED_ALARM_MELODY_INDEX, selectedPosition)
        editor.apply()

        alarmViewModel.setChosenAlarmSound(alarmSound)
    }
}
