package com.developers.sleep.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentHomeBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    companion object {
        @Volatile
        private var instance: HomeFragment? = null

        fun getInstance(): HomeFragment {
            return instance ?: synchronized(this) {
                instance ?: HomeFragment().also { instance = it }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var userprogress = 0f
        var angle = 0f
        val circularProgressBar = binding.circularProgressBar
        circularProgressBar.apply {
            progress = userprogress
            progressMax = 6f
        }

        binding.buttonGoToTest.setOnClickListener {
            userprogress += 1f
            angle -= 45f
            circularProgressBar.apply {
                progress = userprogress
                startAngle = angle
                setProgressWithAnimation(userprogress, 1000)
            }
        }

        binding.buttonGoToTest.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_testFragment)
        }

        binding.buttonFallIntoADream.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_sleepSettingsFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
