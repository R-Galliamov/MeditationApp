package com.developers.sleep.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
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
import com.developers.sleep.MediaPlayerHelper
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentSleepPlayerBinding
import com.developers.sleep.viewModel.AlarmViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SleepPlayerFragment : Fragment() {

    private var _binding: FragmentSleepPlayerBinding? = null
    private val binding: FragmentSleepPlayerBinding
        get() = _binding!!

    private val alarmViewModel: AlarmViewModel by activityViewModels()

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

        // TODO show player to user
        //binding.unfoldingLayout.visibility = View.VISIBLE
        //Handler().postDelayed({
        //toggleUnfoldedLayout()}, 3000)

        binding.buttonStop.setOnClickListener {
            mediaPlayerHelper.stopPlaying()
            findNavController().navigateUp()
        }

        binding.buttonMiniPlayer.setOnClickListener {
            it.isEnabled = false
            toggleUnfoldedLayout()
            it.isEnabled = true
        }
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
        window.setBackgroundDrawable(null)
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