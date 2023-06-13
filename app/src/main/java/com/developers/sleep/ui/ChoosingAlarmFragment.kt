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
import com.developers.sleep.adapter.AlarmSoundAdapter
import com.developers.sleep.dataModels.AlarmSound
import com.developers.sleep.databinding.FragmentChoosingAlarmBinding
import com.developers.sleep.service.AlarmSoundPlayerHelper
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
    lateinit var mediaPlayerHelper: AlarmSoundPlayerHelper

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
            override fun onMelodyClick(alarmSound: AlarmSound) {
                mediaPlayerHelper.playAlarmSound(alarmSound.fileName)
                saveSelectedMelody(alarmSound)
            }
        }, alarmViewModel.chosenAlarmSound.value!!)

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        val alarmSounds = alarmViewModel.getAlarmSoundsList()
        adapter.submitList(alarmSounds)

        binding.buttonBack.setOnClickListener {
            closeFragment()
        }
        binding.buttonChoose.setOnClickListener {
            closeFragment()
        }
        return binding.root
    }

    private fun closeFragment() {
        val backStackCount = parentFragmentManager.backStackEntryCount

        if (backStackCount > 0) {
            val lastBackStackEntry = parentFragmentManager.getBackStackEntryAt(backStackCount - 1)
            val lastFragmentTag = lastBackStackEntry.name

            if (lastFragmentTag == "Settings") {
                parentFragmentManager.popBackStack()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerHelper.stopPlaying()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveSelectedMelody(alarmSound: AlarmSound) {
        alarmViewModel.setChosenAlarmSound(alarmSound)
    }
}
