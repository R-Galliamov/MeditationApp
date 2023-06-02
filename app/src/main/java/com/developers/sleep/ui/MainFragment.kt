package com.developers.sleep.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.developers.sleep.ColorConstants
import com.developers.sleep.R
import com.developers.sleep.databinding.FragmentMainBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    companion object {
        @Volatile
        private var instance: MainFragment? = null

        fun getInstance(): MainFragment {
            return instance ?: synchronized(this) {
                instance ?: MainFragment().also { instance = it }
            }
        }
    }

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

        val bottomNavView = binding.bottomNavView
        bottomNavView.itemIconTintList = null
        showFragment(HomeFragment())



        //TODO check what fragment is now shown
        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuHome -> {
                    showFragment(HomeFragment())
                }
                R.id.menuSleep -> {
                    //TODO change tab to home after coming back
                    findNavController().navigate(R.id.action_mainFragment_to_sleepSettingsFragment)
                }
                R.id.menuMelodies -> {
                    showFragment(ChoosingGoalFragment())
                }
                R.id.menuProfile -> {
                    showFragment(ChoosingGoalFragment())
                }
            }
            true
        }
    }

    private fun showFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerSmall, fragment)
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
