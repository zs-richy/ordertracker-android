package hu.unideb.inf.ordertracker_android.network

import android.util.Log
import hu.unideb.inf.ordertracker_android.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiDataSource() {


    suspend fun authenticateClient(request: LoginRequest) = CustomSuspendCall(request, LoginResponse::class.java, {
        BackendApi.retrofitService.authenticateClient(request)
    }).callFunction()

//    suspend fun authenticateClient(request: LoginRequest): LoginResponse? {
//        try {
//            val response: Response<LoginResponse>
//
//            response =
//                withContext(Dispatchers.IO) { BackendApi.retrofitService.authenticateClient(request) }
//            Log.d("RESPONSE", response.toString())
//
//            return response.body()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return null
//        }
//    }

    suspend fun getProductsNew() = CustomSuspendCall(ProductsRequest(), ProductsDto::class.java, { request ->
        BackendApi.retrofitService.getProducts(request)
    }).callFunction()

    suspend fun getProducts(): ProductsDto? {
        try {
            var response: ProductsDto? = null

            withContext(Dispatchers.IO) {
                var apiResp = BackendApi.retrofitService.getProducts(ProductsRequest().also { it.imageType = "THUMBNAIL" })
                if (apiResp.isSuccessful) {
                    apiResp.body()?.let { response = it }
                    Log.d("RESPONSE", apiResp.toString())
                } else {
                    response = NetworkUtils.parseErrorBody(apiResp.errorBody()?.string(), ProductsDto::class.java)
                }
            }

            return response
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

//    suspend fun placeOrder(placeOrderRequest: PlaceOrderRequest): Boolean {
//        var orderSuccess = false
//
//        try {
//            val response = withContext(Dispatchers.IO) {
//                BackendApi.retrofitService.placeOrder(placeOrderRequest)
//            }
//            if (response.isSuccessful) {
//                orderSuccess = true
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return orderSuccess
//    }

    suspend fun placeOrder(placeOrderRequest: PlaceOrderRequest) = CustomSuspendCall(placeOrderRequest, NewOrderResponse::class.java, {
        BackendApi.retrofitService.placeOrder(placeOrderRequest)
    }).callFunction()

    suspend fun getOrders() = CustomSuspendCall(Unit, OrdersResponse::class.java, {
        BackendApi.retrofitService.getOrders()
    }).callFunction()

    suspend fun getOrdersNoJoin() = CustomSuspendCall(Unit, OrdersResponse::class.java, {
        BackendApi.retrofitService.getOrdersNoJoin()
    }).callFunction()

    suspend fun getOrderById(orderId: Long) = CustomSuspendCall(orderId, GetOrderByIdResponse::class.java, {
        BackendApi.retrofitService.getOrderById(orderId)
    }).callFunction()

    suspend fun signUp(signupRequest: SignupRequest) = CustomSuspendCall(signupRequest, SignupResponse::class.java, {
      BackendApi.retrofitService.signUp(signupRequest)
    }).callFunction()

}