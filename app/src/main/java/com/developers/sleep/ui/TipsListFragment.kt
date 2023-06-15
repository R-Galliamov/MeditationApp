package com.developers.sleep.ui

import TipsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentTipsListBinding
import com.developers.sleep.databinding.TipItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TipsListFragment : Fragment() {
    private var _binding: FragmentTipsListBinding? = null
    private val binding: FragmentTipsListBinding
        get() = _binding!!
    private val tipsViewModel: TipsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTipsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }


        tipsViewModel.tipOfTheDay.observe(viewLifecycleOwner) { tip ->
            binding.tipNameText.text = tip.name
            binding.coloredRectangle.setBackgroundResource(tip.panelRes)
        }

        val tipOfTheDay = tipsViewModel.tipOfTheDay.value

        binding.buttonTip.setOnClickListener {
            tipsViewModel.setCurrentTip(tipOfTheDay!!)
            findNavController().navigate(R.id.action_tipsListFragment_to_tipFragment)
        }

        binding.coloredRectangle.setOnClickListener {
            tipsViewModel.setCurrentTip(tipOfTheDay!!)
            findNavController().navigate(R.id.action_tipsListFragment_to_tipFragment)
        }

        val tips = tipsViewModel.tipsList

        for (tip in tips) {
            val itemView = TipItemBinding.inflate(layoutInflater, binding.linearContainer, false)

            itemView.tipNameText.text = tip.name
            itemView.coloredRectangle.setBackgroundResource(tip.panelRes)
            itemView.imageTip.setBackgroundResource(tip.imageRes)
            itemView.buttonTip.setOnClickListener {
                tipsViewModel.setCurrentTip(tip)
                findNavController().navigate(R.id.action_tipsListFragment_to_tipFragment)
            }

            itemView.coloredRectangle.setOnClickListener {
                tipsViewModel.setCurrentTip(tip)
                findNavController().navigate(R.id.action_tipsListFragment_to_tipFragment)
            }
            binding.linearContainer.addView(itemView.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
