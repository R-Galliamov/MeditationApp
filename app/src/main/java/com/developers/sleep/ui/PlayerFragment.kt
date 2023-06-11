package com.developers.sleep.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentPlayerBinding
import com.developers.sleep.service.MediaPlayerHelper
import com.developers.sleep.viewModel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding
        get() = _binding!!

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private lateinit var timerJob: Job
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var currentMelody = playerViewModel.currentMelody.value
        var durationInMinutes = playerViewModel.musicDurationInMinutes.value ?: 30
        timerJob = updatingTimerJob(durationInMinutes)
        mediaPlayerHelper.setDuration(durationInMinutes)
        mediaPlayerHelper.playMelodyByUrl(currentMelody?.uri.toString()) //TODO its not uri. Its file name
        binding.timerText.text = formatMinutesToMinutesSeconds(durationInMinutes)
        playerViewModel.currentMelody.observe(viewLifecycleOwner)
        {
            currentMelody = it
            binding.melodyNameText.text = it.name
        }

        playerViewModel.currentPlaylist.observe(viewLifecycleOwner)
        {
            binding.playlistNameText.text = it.name
        }

        playerViewModel.musicDurationInMinutes.observe(viewLifecycleOwner) {
            durationInMinutes = it
            mediaPlayerHelper.setDuration(durationInMinutes)
            timerJob.cancel()
            timerJob = updatingTimerJob(durationInMinutes)
            if (mediaPlayerHelper.isMelodyPlaying.value == true) {
                timerJob.start()
            }
            binding.timerText.text = formatMinutesToMinutesSeconds(durationInMinutes)
        }

        mediaPlayerHelper.isMelodyPlaying.observe(viewLifecycleOwner) { state ->
            if (state == true) {
                binding.buttonPlayer.setBackgroundResource(R.drawable.button_pause)
            } else {
                binding.buttonPlayer.setBackgroundResource(R.drawable.rounded_button_play)
            }
        }

        binding.numberPicker.apply {
            minValue = 1
            maxValue = 59
            value = durationInMinutes
        }

        binding.buttonPlayer.setOnClickListener {
            if (mediaPlayerHelper.isMelodyPlaying.value == true) {
                mediaPlayerHelper.pausePlaying()
            } else {
                if (timerJob.isActive) {
                    mediaPlayerHelper.resumePlaying()
                } else {
                    mediaPlayerHelper.setDuration(durationInMinutes)
                    mediaPlayerHelper.playMelodyByUrl(currentMelody?.uri.toString())
                    timerJob.cancel()
                    timerJob = updatingTimerJob(durationInMinutes)
                    timerJob.start()
                }
            }
        }

        binding.numberPickerContainer.setOnClickListener {
            binding.numberPicker.visibility = View.VISIBLE
            binding.timerText.visibility = View.GONE
        }

        binding.root.setOnClickListener {
            if (binding.numberPicker.visibility == View.VISIBLE) {
                binding.numberPicker.visibility = View.GONE
                binding.timerText.visibility = View.VISIBLE
            }
        }

        binding.numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            playerViewModel.setMusicDurationInMinutes(newVal)
        }

        binding.buttonBack.setOnClickListener() {
            findNavController().navigateUp()
        }
    }

    private fun updatingTimerJob(minutes: Int): Job {
        val totalSeconds = minutes * 60
        var secondsRemaining = totalSeconds

        return CoroutineScope(Dispatchers.Default).launch {
            while (secondsRemaining > 0) {
                if (mediaPlayerHelper.isMelodyPlaying.value == true) {
                    val minutesPart = secondsRemaining / 60
                    val secondsPart = secondsRemaining % 60
                    val timeText = String.format("%02d:%02d", minutesPart, secondsPart)
                    withContext(Dispatchers.Main) {
                        binding.timerText.text = timeText
                        val progress = (totalSeconds - secondsRemaining).toFloat()
                        updateProgressBar(progress, totalSeconds.toFloat())
                    }

                    delay(1000)
                    secondsRemaining--
                } else {
                    delay(1000)
                }
            }
            withContext(Dispatchers.Main) {
                binding.timerText.text = "00:00"
                updateProgressBar(totalSeconds.toFloat(), totalSeconds.toFloat())
            }
        }
    }

    private fun updateProgressBar(currentProgress: Float, maxProgress: Float) {
        binding.circularProgressBar.apply {
            progress = currentProgress
            progressMax = maxProgress
        }
    }

    private fun formatMinutesToMinutesSeconds(minutes: Int): String {
        val minutesPart = minutes % 60
        val secondsPart = 0
        return String.format("%02d:%02d", minutesPart, secondsPart)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerHelper.pausePlaying()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayerHelper.stopPlaying()
        mediaPlayerHelper.resetTrackProgress()
        timerJob.cancel()
        _binding = null
    }

}
