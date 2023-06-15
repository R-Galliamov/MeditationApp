package com.developers.sleep.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.developers.sleep.ui.HomeFragment

class MenuViewModel : ViewModel() {

    val currentFragment = MutableLiveData<Fragment>()

    init {
        currentFragment.value = HomeFragment()
    }

}
