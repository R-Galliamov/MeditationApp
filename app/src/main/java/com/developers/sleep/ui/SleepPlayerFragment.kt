package com.developers.sleep.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developers.sleep.ColorConstants
import com.developers.sleep.service.MediaPlayerHelper
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentSleepPlayerBinding
import com.developers.sleep.viewModel.AlarmViewModel
import com.developers.sleep.viewModel.AlarmPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SleepPlayerFragment : Fragment() {

    private var _binding: FragmentSleepPlayerBinding? = null
    private val binding: FragmentSleepPlayerBinding
        get() = _binding!!

    private val alarmViewModel: AlarmViewModel by activityViewModels()
    private val alarmPlayerViewModel: AlarmPlayerViewModel by activityViewModels()

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var updateTimeJob: Job? = null


    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSleepPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()
        val currentMelody = alarmPlayerViewModel.currentMelody //TODO pass it to the mediaPlayer

        if (alarmViewModel.isMusicOn()) {
            binding.buttonMiniPlayer.visibility = View.VISIBLE
            mediaPlayerHelper.startPlayLoopingAlarmSound("IterstellarTheme.mp3") //TODO replace with melody and duration
            binding.buttonPlayerState.setBackgroundResource(R.drawable.circular_button_miniplayer_pause)
        } else {
            binding.buttonMiniPlayer.visibility = View.GONE
        }

        with(binding) {
            buttonPlayerState.setOnClickListener {
                if (mediaPlayerHelper.isPlaying) {
                    mediaPlayerHelper.pausePlaying()
                    buttonPlayerState.setBackgroundResource(R.drawable.circular_button_miniplayer_play)
                } else {
                    mediaPlayerHelper.continuePlaying()
                    buttonPlayerState.setBackgroundResource(R.drawable.circular_button_miniplayer_pause)
                }
            }
            songTitle.text = currentMelody.value?.name ?: "Interstellar" //TODO remove
        }

        with(binding) {
            currentTimeText.text = getCurrentTime()
            val alarmTime = alarmViewModel.alarmTime.value
            alarmTimeText.text = timeFormat.format(alarmTime?.time)
            buttonStop.setOnClickListener {
                mediaPlayerHelper.stopPlaying()
                findNavController().navigateUp()
            }
            buttonMiniPlayer.setOnClickListener {
                it.isEnabled = false
                toggleUnfoldedLayout()
                it.isEnabled = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateTimeJob = startUpdatingTime()
    }

    override fun onPause() {
        super.onPause()
        updateTimeJob?.cancel()
    }


    private fun startUpdatingTime(): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val formattedTime = getCurrentTime()
                withContext(Dispatchers.Main) {
                    binding.currentTimeText.text = formattedTime
                }
                delay(1000)
            }
        }
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        return timeFormat.format(calendar.time)
    }

    private fun setBackground() {
        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.setBackgroundDrawableResource(R.drawable.sleep_diver_background)
    }

    //TODO test if margins to small
    private fun removeBackground() {
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.decorView.systemUiVisibility = 0
        val rootView = requireActivity().window.decorView.rootView
        rootView.setBackgroundColor(ColorConstants.EERIE_BLACK)
    }

    private fun toggleUnfoldedLayout() {
        val unfoldedLayout = binding.unfoldingLayout
        val rightButton = binding.buttonMiniPlayer
        val rightButtonCenterX = rightButton.left + rightButton.width / 2
        if (unfoldedLayout.visibility == View.VISIBLE) {
            // Collapse the layout
            val animator = ObjectAnimator.ofFloat(
                unfoldedLayout,
                "translationX",
                0f,
                rightButtonCenterX.toFloat()
            )
            animator.duration = 300
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    unfoldedLayout.visibility = View.GONE
                }
            })
            animator.start()
        } else {
            // Expand the layout
            unfoldedLayout.visibility = View.VISIBLE
            unfoldedLayout.translationX = rightButtonCenterX.toFloat()
            unfoldedLayout.animate()
                .translationX(0f)
                .setDuration(300)
                .start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeBackground()
        _binding = null
    }


}