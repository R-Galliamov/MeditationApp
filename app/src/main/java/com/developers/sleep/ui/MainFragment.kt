package com.developers.sleep.ui

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.developers.sleep.ColorConstants
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentMainBinding
import com.developers.sleep.viewModel.MenuViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private lateinit var bottomNavView: BottomNavigationView
    private val menuViewModel: MenuViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setNavigationBarColor(requireActivity(), ColorConstants.YANKEES_BLUE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavView = binding.bottomNavView
        bottomNavView.itemIconTintList = null

        showFragment(menuViewModel.currentFragment)
        if (menuViewModel.currentFragment is HomeFragment) {
            updateNavBarUi(R.id.menuHome)
        }

        bottomNavView.setOnItemSelectedListener { menuItem ->
            updateNavBarUi(menuItem.itemId)
            when (menuItem.itemId) {
                R.id.menuHome -> {
                    val fragment = HomeFragment()
                    menuViewModel.currentFragment = fragment
                    showFragment(fragment)
                }

                R.id.menuSleep -> {
                    val fragment = HomeFragment()
                    menuViewModel.currentFragment = fragment
                    findNavController().navigate(R.id.action_mainFragment_to_sleepSettingsFragment)
                }

                R.id.menuMelodies -> {
                    val fragment = MelodiesFragment()
                    menuViewModel.currentFragment = fragment
                    showFragment(MelodiesFragment())
                }

                R.id.menuProfile -> {
                    val fragment = ProfileFragment()
                    menuViewModel.currentFragment = fragment
                    showFragment(ProfileFragment())
                }
            }
            true
        }


    }

    private fun updateNavBarUi(menuItemId: Int) {
        with(bottomNavView) {
            val menuHomeItem = menu.findItem(R.id.menuHome)
            val menuSleepItem = menu.findItem(R.id.menuSleep)

            if (menuItemId == R.id.menuHome) {
                menuHomeItem.setIcon(R.drawable.menuhome_selected)
                val spannableStringSelected = SpannableString(menuHomeItem.title)
                spannableStringSelected.setSpan(
                    ForegroundColorSpan(ColorConstants.BLUE_LIGHT),
                    0,
                    spannableStringSelected.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                menuHomeItem.title = spannableStringSelected

                menuSleepItem.setIcon(R.drawable.menumoon)

                val spannableStringUnselected = SpannableString(menuSleepItem.title)
                spannableStringUnselected.setSpan(
                    ForegroundColorSpan(ColorConstants.WHITE),
                    0,
                    spannableStringUnselected.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                menuSleepItem.title = spannableStringUnselected

            } else {
                menuHomeItem.setIcon(R.drawable.menuhome)
                val spannableStringUnselected = SpannableString(menuHomeItem.title)
                spannableStringUnselected.setSpan(
                    ForegroundColorSpan(ColorConstants.WHITE),
                    0,
                    spannableStringUnselected.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                menuHomeItem.title = spannableStringUnselected
            }
        }
    }


    private fun showFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setNavigationBarColor(requireActivity(), ColorConstants.EERIE_BLACK)
        _binding = null
    }

    private fun setNavigationBarColor(activity: FragmentActivity, color: Int) {
        val window: Window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = color
    }
}
