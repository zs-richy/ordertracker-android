package hu.unideb.inf.ordertracker_android.network

class ApiDataSource() {

    suspend fun authenticateClient(request: LoginRequest) =
        CustomSuspendCall(request, LoginResponse::class.java, {
            BackendApi.retrofitService.authenticateClient(request)
        }).callFunction()

    suspend fun getProductsNew() =
        CustomSuspendCall(ProductsRequest(), ProductsDto::class.java, { request ->
            BackendApi.retrofitService.getProducts(request)
        }).callFunction()

    suspend fun placeOrder(placeOrderRequest: PlaceOrderRequest) =
        CustomSuspendCall(placeOrderRequest, NewOrderResponse::class.java, {
            BackendApi.retrofitService.placeOrder(placeOrderRequest)
        }).callFunction()

    suspend fun getOrdersNoJoin() =
        CustomSuspendCall(Unit, OrdersResponse::class.java, {
            BackendApi.retrofitService.getOrdersNoJoin()
        }).callFunction()

    suspend fun getOrderById(orderId: Long) =
        CustomSuspendCall(orderId, GetOrderByIdResponse::class.java, {
            BackendApi.retrofitService.getOrderById(orderId)
        }).callFunction()

    suspend fun signUp(signupRequest: SignupRequest) =
        CustomSuspendCall(signupRequest, SignupResponse::class.java, {
            BackendApi.retrofitService.signUp(signupRequest)
        }).callFunction()

    suspend fun getWelcomeMessage() =
        CustomSuspendCall(Unit, WelcomeResponse::class.java, {
            BackendApi.retrofitService.getWelcomeMessage()
        }).callFunction()

}