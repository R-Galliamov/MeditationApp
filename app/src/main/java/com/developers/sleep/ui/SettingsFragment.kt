package com.developers.sleep.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.apphud.sdk.Apphud
import com.developers.sleep.GeneralPrefs
import com.developers.sleep.PRIVACY_POLICY_URL
import com.developers.sleep.R
import com.developers.sleep.SUPPORT_EMAIL
import com.developers.sleep.TERMS_OF_USE_URL
import com.developers.sleep.databinding.FragmentSettingsBinding
import com.developers.sleep.databinding.FragmentTestBinding
import com.developers.sleep.service.MediaPlayerHelper
import com.developers.sleep.viewModel.AlarmViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val alarmViewModel: AlarmViewModel by activityViewModels()

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper
    private lateinit var generalSP: SharedPreferences

    private var volume: Float? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.notificationSwitcher.isChecked = areNotificationsEnabled()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generalSP =
            requireActivity().getSharedPreferences(GeneralPrefs.PREFS_NAME, Context.MODE_PRIVATE)
        volume = generalSP.getFloat(GeneralPrefs.VOLUME, 1f)
        binding.sliderVolume.value = volume!! * 100f

        binding.notificationSwitcher.setOnClickListener {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            startActivity(intent)
        }

        binding.buttonAlarmSoundText.text = alarmViewModel.chosenAlarmSound.value?.name

        binding.buttonAlarmChoosing.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ChoosingAlarmFragment())
                .addToBackStack("Settings")
                .commit()
        }

        binding.sliderVolume.addOnChangeListener { slider, value, fromUser ->
            volume = value / 100f
            mediaPlayerHelper.setVolume(volume!!)
        }

        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.buttonTermsOfUse.setOnClickListener {
            val url = TERMS_OF_USE_URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        binding.buttonRestorePurchase.setOnClickListener {
            Apphud.restorePurchases { subscriptions, purchases, error -> }
        }

        binding.buttonSupport.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:$SUPPORT_EMAIL")
            startActivity(emailIntent)
        }
    }

    private fun saveVolume(volume: Float) {
        generalSP.edit().putFloat(GeneralPrefs.VOLUME, volume).apply()
    }

    private fun areNotificationsEnabled(): Boolean {
        val notificationManager = NotificationManagerCompat.from(requireContext())
        return notificationManager.areNotificationsEnabled()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveVolume(volume!!)
        _binding = null
    }

}
