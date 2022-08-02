package hu.unideb.inf.ordertracker_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hu.unideb.inf.ordertracker_android.model.api.Order
import hu.unideb.inf.ordertracker_android.network.ApiDataSource
import kotlinx.coroutines.launch

class OrdersViewModel(application: Application): AndroidViewModel(application) {

    private val dataSource = ApiDataSource()

    private var _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>>
        get() = _orders

    private var _isFetchingInProgress = MutableLiveData<Boolean>()
    val isFetchingInProgress: LiveData<Boolean>
        get() = _isFetchingInProgress

    fun fetchOrders() {
        if (_isFetchingInProgress.value == true) return
        _isFetchingInProgress.value = true

        viewModelScope.launch {
            var ordersResponse = dataSource.getOrdersNoJoin()

            ordersResponse.orders?.let { _orders.value = it }
            _isFetchingInProgress.value = false
        }
    }

}