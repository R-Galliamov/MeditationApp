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
import com.developers.sleep.AlarmSound
import com.developers.sleep.GeneralViewModel
import com.developers.sleep.PrefsConstants
import com.developers.sleep.adapter.MelodyAdapter
import com.developers.sleep.databinding.FragmentChoosingAlarmBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChoosingAlarmFragment : Fragment() {
    private var _binding: FragmentChoosingAlarmBinding? = null
    private val binding: FragmentChoosingAlarmBinding
        get() = _binding!!

    companion object {

        @Volatile
        private var instance: ChoosingAlarmFragment? = null

        fun getInstance(): ChoosingAlarmFragment {
            return instance ?: synchronized(this) {
                instance ?: ChoosingAlarmFragment().also { instance = it }
            }
        }
    }

    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: GeneralViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChoosingAlarmBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(
            PrefsConstants.PREFS_GENERAL_NAME,
            Context.MODE_PRIVATE
        )

        val adapter = MelodyAdapter(object : MelodyAdapter.OnMelodyClickListener {
            override fun onMelodyClick(alarmSound: AlarmSound, position: Int) {
                viewModel.playMelody(alarmSound)
                saveSelectedMelody(alarmSound, position)
            }
        }, getSelectedMelodyIndex() ?: 0)

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        val alarmSounds = viewModel.getAlarmSoundsList()
        adapter.submitList(alarmSounds)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonChoose.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopPlaying()
        _binding = null
    }

    private fun getSelectedMelodyIndex(): Int? {
        return sharedPreferences.getInt(PrefsConstants.SELECTED_ALARM_MELODY_INDEX, -1)
            .takeIf { it >= 0 }
    }

    private fun saveSelectedMelody(alarmSound: AlarmSound, position: Int) {
        val selectedPosition = if (position >= 0) position else 0
        val editor = sharedPreferences.edit()
        editor.putInt(PrefsConstants.SELECTED_ALARM_MELODY_INDEX, selectedPosition)
        editor.putString(PrefsConstants.SELECTED_ALARM_MELODY_NAME, alarmSound.name)
        editor.apply()
    }

}
