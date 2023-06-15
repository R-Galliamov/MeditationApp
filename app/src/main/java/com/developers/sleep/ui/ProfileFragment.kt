package com.developers.sleep.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.developers.sleep.AlarmPlayerPrefs
import com.developers.sleep.R
import com.developers.sleep.UserDataPrefs
import com.developers.sleep.databinding.FragmentProfileBinding
import com.developers.sleep.viewModel.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    private lateinit var userDataSP: SharedPreferences
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDataSP =
            requireActivity().getSharedPreferences(UserDataPrefs.PREFS_NAME, Context.MODE_PRIVATE)
        val userName = userDataSP.getString(UserDataPrefs.USER_NAME, null)
        if (userName.isNullOrBlank()) {
            binding.name.visibility = View.GONE
            binding.fragmentText.text = getString(R.string.fillInInformation)
            binding.notification.visibility = View.VISIBLE
            binding.fragmentText.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, MyAccountFragment())
                    .addToBackStack("My account")
                    .commit()
            }
        } else {
            binding.name.visibility = View.VISIBLE
            binding.name.text = "$userName, "
            binding.fragmentText.text = getString(R.string.good_night)
            //  binding.notification.visibility = View.GONE //TODO decide something with i icon
        }

        val avatarUri = userDataSP.getString(UserDataPrefs.AVATAR_URI, null)

        if (avatarUri != null) {
            Glide.with(this)
                .load(avatarUri.toUri())
                .circleCrop()
                .placeholder(R.drawable.avatar_sample)
                .error(R.drawable.avatar_sample)
                .into(binding.buttonAvatar)
        } else {
            binding.buttonAvatar.setBackgroundResource(R.drawable.avatar_sample)
        }

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { selectedImageUri ->
                Glide.with(this)
                    .load(selectedImageUri)
                    .circleCrop()
                    .into(binding.buttonAvatar)
                userDataSP.edit().putString(UserDataPrefs.AVATAR_URI, selectedImageUri.toString())
                    .apply()
            }

        binding.buttonAvatar.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.buttonSetting.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingsFragment())
                .addToBackStack("Profile")
                .commit()
        }

        binding.name.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MyAccountFragment())
                .addToBackStack("My account")
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
