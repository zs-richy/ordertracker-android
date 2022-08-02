package hu.unideb.inf.ordertracker_android.network

import hu.unideb.inf.ordertracker_android.model.api.Order
import hu.unideb.inf.ordertracker_android.model.api.Product

enum class OrderStatus {
    CREATED,
    WORKING,
    PREPARING,
    DELIVERING,
    COMPLETED
}

abstract class BaseResponse {
    var succeeded: Boolean = false
    var errorMessages: ArrayList<String>? = arrayListOf("API error")
}

class ProductsDto: BaseResponse() {
    var productList: List<Product> = mutableListOf()
}

class NewOrderResponse: BaseResponse() {
    var orderId: Long? = null
    var orderStatus: OrderStatus? = null
}

class OrdersResponse: BaseResponse() {
    var orders: List<Order>? = null
}

class GetOrderByIdResponse: BaseResponse() {
    var order: Order? = null
}

class SignupResponse: BaseResponse()

class LoginResponse: BaseResponse() {
    var token: String? = null
    var username: String? = null
    var roles: MutableList<String> = mutableListOf()
    var expires: Long? = null
}


class PlaceOrderRequest {
    var orderItems: HashMap<Long, Int> = hashMapOf()
}

class SignupRequest {
    var username: String? = null
    var password: String? = null
}

class LoginRequest {
    var username: String? = null
    var password: String? = null
}

class ProductsRequest {
    var imageType: String? = null
}
