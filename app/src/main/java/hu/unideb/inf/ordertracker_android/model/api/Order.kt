package hu.unideb.inf.ordertracker_android.model.api

import hu.unideb.inf.ordertracker_android.network.OrderStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Order {
    val id: Long? = null
    val userId: Long? = null
    val status: OrderStatus? = null
    val orderItems: Set<OrderItem>? = null
    val createdAt: LocalDateTime? = null
    val statusLastModify: LocalDateTime? = null
    val sumOfItems: Long? = null
}