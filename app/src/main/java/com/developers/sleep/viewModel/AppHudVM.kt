package com.developers.sleep.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apphud.sdk.domain.ApphudProduct
import com.developers.sleep.PaywallConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppHudVM @Inject constructor() : ViewModel() {


    private val _products = MutableLiveData<List<ApphudProduct>>()
    val products: LiveData<List<ApphudProduct>>
        get() = _products

    private val _selectedSubscription = MutableLiveData<Int>()
    val selectedSubscription: LiveData<Int>
        get() = _selectedSubscription

    init {
        selectSubscription(PaywallConstants.YEAR_PAYMENT)
    }

    fun setProducts(products: List<ApphudProduct>) {
        _products.value = products
    }

    fun selectSubscription(subscriptionId: Int) {
        _selectedSubscription.value = subscriptionId
    }
}
