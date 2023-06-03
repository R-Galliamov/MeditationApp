package com.developers.sleep.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.developers.sleep.AlarmSound
import com.developers.sleep.ColorConstants
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentMainBinding
import com.developers.sleep.databinding.FragmentSleepDiverBinding
import com.developers.sleep.viewModel.AlarmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SleepDiverFragment : Fragment() {

    private var _binding: FragmentSleepDiverBinding? = null
    private val binding: FragmentSleepDiverBinding
        get() = _binding!!

    private val alarmViewModel: AlarmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSleepDiverBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rightButton.setOnClickListener{
            toggleUnfoldedLayout()
        }
    }

    private fun toggleUnfoldedLayout() {
        val unfoldedLayout = binding.unfoldedLayout
        val rightButton = binding.rightButton
        val rightButtonCenterX = rightButton.left + rightButton.width / 2
        val rightButtonCenter = rightButton.width / 2

        //TODO add code if movement wasnt ended
        if (unfoldedLayout.visibility == View.VISIBLE) {
            // Collapse the layout
            val animator = ObjectAnimator.ofFloat(unfoldedLayout, "translationX", 0f, rightButtonCenterX.toFloat())
            animator.duration = 3000
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
                .setDuration(3000)
                .start()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}