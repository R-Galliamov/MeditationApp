package com.developers.sleep.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentPaywallBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaywallFragment : Fragment() {
    private var _binding: FragmentPaywallBinding? = null
    private val binding: FragmentPaywallBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaywallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yearPayment = true

        with(binding) {
            buttonWeeklyPayment.setOnClickListener {
                it.setBackgroundResource(R.drawable.rounded_button_blue)
                buttonYearPayment.setBackgroundResource(R.drawable.rounded_ripple_button)
            }
            buttonYearPayment.setOnClickListener {
                it.setBackgroundResource(R.drawable.rounded_button_blue)
                buttonWeeklyPayment.setBackgroundResource(R.drawable.rounded_ripple_button)
            }


            buttonNext.setOnClickListener {
                if (yearPayment) {
                    //TODO implement navigation
                } else {

                }


            }

            buttonClose.setOnClickListener {
                if (isFragmentShownOnAppStart()) {
                    findNavController().navigate(R.id.action_paywallFragment_to_mainFragment)
                }
            }
        }


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isFragmentShownOnAppStart()) {
                        requireActivity().finish()
                    }
                }
            })


    }

    private fun isFragmentShownOnAppStart(): Boolean {
        val backStackCount = findNavController().backStack.size
        return backStackCount == 3
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
