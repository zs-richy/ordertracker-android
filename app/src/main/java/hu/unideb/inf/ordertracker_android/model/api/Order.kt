package hu.unideb.inf.ordertracker_android.model.api

import hu.unideb.inf.ordertracker_android.network.OrderStatus
import java.util.*

class Order {
    val id: Long? = null
    val userId: Long? = null
    val status: OrderStatus? = null
    val orderItems: Set<OrderItem>? = null
    val createdAt: Date? = null
    val statusLastModify: Date? = null
    val sumOfItems: Long? = null
}