package hu.unideb.inf.ordertracker_android.model.api

import hu.unideb.inf.ordertracker_android.model.api.Image

data class Product(
    val id: Long,
    val name: String,
    val price: Int,
    var images: Set<Image>,
    var count: Int
)