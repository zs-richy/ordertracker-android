package hu.unideb.inf.ordertracker_android.viewmodel

import android.app.Application
import androidx.lifecycle.*
import hu.unideb.inf.ordertracker_android.model.api.Product
import hu.unideb.inf.ordertracker_android.network.ApiDataSource
import hu.unideb.inf.ordertracker_android.network.NewOrderResponse
import hu.unideb.inf.ordertracker_android.network.PlaceOrderRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel(application: Application): AndroidViewModel(application) {
    enum class PlacingOrderStatus {
        WAITING,
        PLACING,
        FINISHED,
        ERROR
    }

    val dataSource = ApiDataSource()

    val cart = MutableLiveData<HashSet<Product>>(hashSetOf())

    var placedOrderId: Long? = null

    val placingOrderStatus = MutableLiveData(PlacingOrderStatus.WAITING)

    val isEmptyCart = Transformations.map(cart) { cart ->
        cart.isNullOrEmpty()
    }

    val cartItemsToView = Transformations.map(cart) { cart ->
        if (cart.isNullOrEmpty()) {
            ""
        } else {
            "${calculateCartSize()} item(s)"
        }
    }

    val cartTotalPrice = Transformations.map(cart) { cart ->
        if (cart.isNullOrEmpty()) {
            ""
        } else {
            "Total: ${calculateTotalPrice()}"
        }
    }

    fun clearCart() {
        cart.value = hashSetOf()
        placingOrderStatus.value = PlacingOrderStatus.WAITING
    }

    fun placeOrder() {
        if (placingOrderStatus.value == PlacingOrderStatus.PLACING) return
        viewModelScope.launch {
            placingOrderStatus.value = PlacingOrderStatus.PLACING
            val request = PlaceOrderRequest().also { request ->
                cart.value?.forEach { cartItem ->
                    request.orderItems.put(cartItem.id, cartItem.count)
                }
            }
            val serverResponse = dataSource.placeOrder(request)
            delay(500)

            handleServerResponse(serverResponse)
        }
    }

    private fun handleServerResponse(placeOrderResponse: NewOrderResponse) {
        if (placeOrderResponse.succeeded) {
            placedOrderId = placeOrderResponse.orderId
            clearCart()
            placingOrderStatus.value = PlacingOrderStatus.FINISHED
        } else {
            placingOrderStatus.value = PlacingOrderStatus.ERROR
        }
    }

    fun calculateCartSize(): Int {
        if (cart.value.isNullOrEmpty()) {
            return 0
        } else {
            var sum = 0
            cart.value?.forEach { entry ->
                sum = sum + entry.count
            }

            return sum
        }
    }

    fun calculateTotalPrice(): Int {
        if (cart.value.isNullOrEmpty()) {
            return 0
        } else {
            var sum = 0
            cart.value?.forEach { entry ->
                sum = sum + entry.price * entry.count
            }

            return sum
        }
    }

    fun addItemToCart(product: Product) {
        if (cart.value == null) {
            return
        }

        findProductById(product.id)?.let {
            it.count++
        } ?: run {
            cart.value?.add(product.also { it.count = 1 })
        }

        cart.value = cart.value
    }

    fun deleteItemFromCart(product: Product) {
        findProductById(product.id)?.let {
            cart.value?.remove(it)
            cart.value = cart.value
        }
    }

    fun findProductById(id: Long): Product? {
        return cart.value?.find { prod -> prod.id == id }
    }
}