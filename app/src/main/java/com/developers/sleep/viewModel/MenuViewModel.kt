package com.developers.sleep.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.developers.sleep.ui.HomeFragment

class MenuViewModel : ViewModel() {
    var currentFragment: Fragment = HomeFragment()
}
