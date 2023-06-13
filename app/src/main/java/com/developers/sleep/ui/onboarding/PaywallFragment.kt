package com.developers.sleep.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.SkuDetails
import com.apphud.sdk.Apphud
import com.apphud.sdk.ApphudListener
import com.apphud.sdk.domain.ApphudPaywall
import com.developers.sleep.viewModel.AppHudVM
import com.developers.sleep.PaywallConstants
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentPaywallBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaywallFragment : Fragment() {
    private var _binding: FragmentPaywallBinding? = null
    private val binding: FragmentPaywallBinding
        get() = _binding!!

    private val appHudVM: AppHudVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaywallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appHudVM.selectedSubscription.observe(viewLifecycleOwner) {
            when (it) {
                PaywallConstants.WEEKLY_PAYMENT -> {
                    binding.buttonWeeklyPayment.setBackgroundResource(R.drawable.rounded_button_blue)
                    binding.buttonYearPayment.setBackgroundResource(R.drawable.rounded_ripple_button)
                }

                PaywallConstants.YEAR_PAYMENT -> {
                    binding.buttonWeeklyPayment.setBackgroundResource(R.drawable.rounded_ripple_button)
                    binding.buttonYearPayment.setBackgroundResource(R.drawable.rounded_button_blue)
                }
            }
        }

        with(binding) {
            buttonWeeklyPayment.setOnClickListener {
                appHudVM.selectSubscription(PaywallConstants.WEEKLY_PAYMENT)
            }
            buttonYearPayment.setOnClickListener {
                appHudVM.selectSubscription(PaywallConstants.YEAR_PAYMENT)
            }

            buttonNext.setOnClickListener {
                val product = when (appHudVM.selectedSubscription.value) {
                    PaywallConstants.WEEKLY_PAYMENT -> {
                        appHudVM.products.value?.get(0)
                    }

                    PaywallConstants.YEAR_PAYMENT -> {
                        appHudVM.products.value?.get(1)
                    }

                    else -> null
                }

                product?.let {
                    Apphud.purchase(requireActivity(), it) { result ->
                        if (Apphud.hasPremiumAccess())
                            findNavController().navigate(R.id.action_paywallFragment_to_mainFragment)
                    }
                }
            }

            buttonClose.setOnClickListener {
                if (isFragmentShownOnAppStart()) {
                    findNavController().navigate(R.id.action_paywallFragment_to_mainFragment)
                } else {
                    findNavController().navigateUp()
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


        Apphud.setListener(object : ApphudListener {
            override fun apphudDidChangeUserID(userId: String) {}

            override fun apphudFetchSkuDetailsProducts(details: List<SkuDetails>) {}

            override fun paywallsDidFullyLoad(paywalls: List<ApphudPaywall>) {
                val paywall = paywalls.first { it.identifier == "main" }
                val products = paywall.products
                if (products != null) {
                    appHudVM.setProducts(products)
                }
            }

            override fun userDidLoad() {}
        })
    }

    private fun isFragmentShownOnAppStart(): Boolean {
        val backStackCount = findNavController().backStack.size
        return backStackCount == 2
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
