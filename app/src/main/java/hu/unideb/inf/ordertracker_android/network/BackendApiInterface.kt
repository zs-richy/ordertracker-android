package hu.unideb.inf.ordertracker_android.network

import retrofit2.Response
import retrofit2.http.*

interface BackendApiInterface {

    @POST("/auth/authenticate")
    suspend fun authenticateClient(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/auth/signup")
    suspend fun signUp(@Body signupRequest: SignupRequest): Response<SignupResponse>

    @POST("/api/product/get-products")
    suspend fun getProducts(@Body productsRequest: ProductsRequest): Response<ProductsDto>

    @POST("/api/order/new-order")
    suspend fun placeOrder(@Body placeOrderRequest: PlaceOrderRequest): Response<NewOrderResponse>

    @GET("/api/order/orders")
    suspend fun getOrders(): Response<OrdersResponse>

    @GET("/api/order/orders-no-join")
    suspend fun getOrdersNoJoin(): Response<OrdersResponse>

    @GET("/api/order/order/{id}")
    suspend fun getOrderById(@Path("id") orderId: Long): Response<GetOrderByIdResponse>

    @GET("/api/general/welcome")
    suspend fun getWelcomeMessage(): Response<WelcomeResponse>

}