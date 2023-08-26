package com.developers.sleep.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding
        get() = _binding!!

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private var timerJob: Job? = null
    private var internetCheckJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var currentMelody = playerViewModel.currentMelody.value
        var durationInMinutes = playerViewModel.musicDurationInMinutes.value ?: 30
        mediaPlayerHelper.setDuration(durationInMinutes)

        mediaPlayerHelper.playMelodyByUrl(currentMelody?.url.toString())

        timerJob = updatingTimerJob(playerViewModel.musicDurationInMinutes.value!!)
        binding.timerText.text = formatMinutesToMinutesSeconds(durationInMinutes)

        playerViewModel.currentMelody.observe(viewLifecycleOwner) {
            currentMelody = it
            binding.melodyNameText.text = it.name
        }

        playerViewModel.currentPlaylist.observe(viewLifecycleOwner) {
            binding.playlistNameText.text = it.name
        }

        playerViewModel.musicDurationInMinutes.observe(viewLifecycleOwner) {
            durationInMinutes = it
            mediaPlayerHelper.setDuration(durationInMinutes)
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
            } else if (isInternetAvailable(requireContext())) {
                mediaPlayerHelper.resumePlaying()
            } else {
                checkInternetConnection(requireContext())
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
            timerJob?.cancel()
            timerJob = updatingTimerJob(newVal)
        }

        binding.buttonBack.setOnClickListener {
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

    private fun startInternetCheckJob(context: Context): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                if (!isInternetAvailable(context)) {
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
                context, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT
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
            return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || activeNetwork.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
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

    override fun onResume() {
        super.onResume()
        internetCheckJob = startInternetCheckJob(requireContext())
    }

    override fun onPause() {
        super.onPause()
        internetCheckJob?.cancel()
        mediaPlayerHelper.pausePlaying()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayerHelper.stopPlaying()
        mediaPlayerHelper.resetTrackProgress()
        timerJob?.cancel()
        _binding = null
    }

}
