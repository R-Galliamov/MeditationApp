package com.developers.sleep.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.text.SimpleDateFormat
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developers.sleep.ACTION_ALARM_TRIGGERED
import com.developers.sleep.ColorConstants
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentSleepPlayerBinding
import com.developers.sleep.service.MediaPlayerHelper
import com.developers.sleep.viewModel.AlarmPlayerViewModel
import com.developers.sleep.viewModel.AlarmViewModel
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

    private var internetCheckJob: Job? = null

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper

    private val alarmTriggeredReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onAlarmTriggered()
        }
    }

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
        val currentMelody = alarmPlayerViewModel.currentMelody
        val musicDuration = alarmPlayerViewModel.musicDurationInMinutes.value ?: 30

        if (alarmViewModel.isMusicOn()) {
            checkInternetConnection(requireContext())
            binding.buttonMiniPlayer.visibility = View.VISIBLE

            mediaPlayerHelper.setDuration(musicDuration)
            mediaPlayerHelper.playMelodyByUrl(currentMelody.value?.url.toString())

        } else {
            binding.buttonMiniPlayer.visibility = View.GONE
        }

        mediaPlayerHelper.isMelodyPlaying.observe(viewLifecycleOwner) { isPlaying ->
            if (isPlaying) {
                binding.buttonPlayerState.setBackgroundResource(R.drawable.circular_button_miniplayer_pause)
            } else {
                binding.buttonPlayerState.setBackgroundResource(R.drawable.circular_button_miniplayer_play)
            }
        }
        with(binding) {
            buttonPlayerState.setOnClickListener {
                if (mediaPlayerHelper.isMelodyPlaying.value == true) {
                    mediaPlayerHelper.pausePlaying()
                } else {
                    checkInternetConnection(requireContext())
                    mediaPlayerHelper.resumePlaying()
                }
            }
            songTitle.text = currentMelody.value?.name ?: "Interstellar" //TODO remove

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
        internetCheckJob = startInternetCheckJob(requireContext())
        val filter = IntentFilter(ACTION_ALARM_TRIGGERED)
        requireContext().registerReceiver(alarmTriggeredReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        updateTimeJob?.cancel()
        internetCheckJob?.cancel()
        requireContext().unregisterReceiver(alarmTriggeredReceiver)
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

    private fun onAlarmTriggered() {
        binding.buttonMiniPlayer.visibility = View.GONE
        binding.buttonStop.setBackgroundResource(R.drawable.rounded_button_crimson)
        binding.buttonText.text = getString(R.string.wake_up)
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
    private fun startInternetCheckJob(context: Context): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                if (!isInternetAvailable(context) && alarmViewModel.isMusicOn()) {
                    withContext(Dispatchers.Main) {
                        mediaPlayerHelper.pausePlaying()
                    }
                }
                delay(5000)
            }
        }
    }

    private fun checkInternetConnection(context: Context) {
        if (!isInternetAvailable(context)) {
            Toast.makeText(
                context,
                getString(R.string.check_your_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayerHelper.stopPlaying()
        removeBackground()
        _binding = null
    }
}
