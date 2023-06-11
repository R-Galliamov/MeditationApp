package com.developers.sleep.ui

import TipsViewModel
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developers.sleep.R
import com.developers.sleep.TestPrefs
import com.developers.sleep.UserDataPrefs
import com.developers.sleep.dataModels.QUESTION_LIST_TEST
import com.developers.sleep.databinding.FragmentHomeBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private var notificationsDialog: AlertDialog? = null

    private val tipsViewModel: TipsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        notificationsDialog?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!areNotificationsEnabled()) {
            showNotificationDialog()
        }

        val userDataSharedPreferences = requireActivity().getSharedPreferences(
            UserDataPrefs.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val nightsCount = userDataSharedPreferences.getInt(UserDataPrefs.NIGHTS_COUNT, 1)

        binding.nightsCountText.text = getString(R.string.night, nightsCount)

        val testSharedPreferences = requireActivity().getSharedPreferences(
            TestPrefs.PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val userAnswersCount = testSharedPreferences.getInt(TestPrefs.USER_ANSWERS_COUNT, 0)
        val userProgress = userAnswersCount.toFloat()
        val questionList = QUESTION_LIST_TEST
        val maxUserProgress = questionList.size.toFloat()
        val nextQuestion =
            if (userAnswersCount < questionList.size) questionList[userAnswersCount].question
            else getString(R.string.test_completed) //TODO replace

        if (userProgress == maxUserProgress) {
            binding.checkbox.visibility = View.VISIBLE
        }

        binding.nextQuestionText.text = nextQuestion
        val angle = (-45f * userProgress)
        val circularProgressBar = binding.circularProgressBar
        circularProgressBar.apply {
            progress = userProgress
            progressMax = maxUserProgress
            startAngle = angle
            setProgressWithAnimation(userProgress, 1000)
        }

        binding.buttonGoToTest.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_testFragment)
        }

        binding.buttonFallIntoADream.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_sleepSettingsFragment)
        }

        val tipOfTheDay = tipsViewModel.tipOfTheDay.value
        binding.tipNameText.text = tipOfTheDay?.name
        binding.coloredRectangle.setBackgroundResource(tipOfTheDay?.drawableRes!!)

        binding.buttonTip.setOnClickListener {
            tipsViewModel.setCurrentTip(tipOfTheDay)
            findNavController().navigate(R.id.action_mainFragment_to_tipFragment)
        }

        binding.coloredRectangle.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_tipFragment)
        }

        binding.readTipsButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_tipsListFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
