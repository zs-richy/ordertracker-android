package hu.unideb.inf.ordertracker_android.viewmodel

import android.app.Application
import android.util.Base64
import android.util.Log
import androidx.lifecycle.*
import hu.unideb.inf.ordertracker_android.network.ApiDataSource
import hu.unideb.inf.ordertracker_android.network.ProductsDto
import hu.unideb.inf.ordertracker_android.util.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsViewModel(application: Application): AndroidViewModel(application) {
    val dataSource = ApiDataSource()

    val products = MutableLiveData<ProductsDto>(null)

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    val sharedFlow =  MutableSharedFlow<String>(0, 5, BufferOverflow.DROP_OLDEST)

    fun initProducts() {
        if (isRefreshing.value == true) return
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(1000)
            processProductsResult(dataSource.getProductsNew())
            Log.d(this.javaClass.toString(), products.toString())
        }
    }

    private fun processProductsResult(products: ProductsDto?) {
        products?.let {
            it.productList.forEach { product ->
                val file = FileUtils.findPath(getApplication(), FileUtils.Directory.IMAGES, product.id.toString() + "_thumb.jpg")
                val imageData = product.images.firstOrNull { it.type == "THUMBNAIL" }?.imageData
                imageData?.let {
                    FileUtils.writeDataToFile(file, Base64.decode(imageData, 0))
                    product.images = emptySet()
                }
            }
            this.products.value = products
        }
        _isRefreshing.value = false
    }

    fun test() {
        if (isRefreshing.value == true) return
        _isRefreshing.value = true
        viewModelScope.launch {
            sharedFlow.emit("start")
            Log.d("FLOW", "start")
            delay(2500)
            sharedFlow.emit( "middle")
            Log.d("FLOW", "middle")
            delay(2500)
            sharedFlow.emit("finish")
            Log.d("FLOW", "finish")
        }
    }


}