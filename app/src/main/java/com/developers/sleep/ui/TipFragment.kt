package com.developers.sleep.ui

import TipsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developers.sleep.databinding.FragmentTipBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TipFragment : Fragment() {
    private var _binding: FragmentTipBinding? = null
    private val binding: FragmentTipBinding
        get() = _binding!!
    private val tipsViewModel: TipsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tipsViewModel.currentTip.observe(viewLifecycleOwner) { tip ->
            binding.tipNameText.text = tip?.name
            binding.tipContentText.text = tip?.content
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
