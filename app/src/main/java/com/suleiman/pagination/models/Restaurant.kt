package com.suleiman.pagination.models

import java.util.*



data class Restaurant(
    val restaurantId:Int,
    val restaurantName: String,
    val restaurantAddress: String,
    val restaurantLocation: String,
    val restaurantRating: Double,
    val restSubscribeDate: String,
    val restaurantSubscribeStatus: String,
    val restaurantRole:String,
    val employeeRestId:Int,
    val totalRevenue:Double,
    val totalOrders:Int,
    val deliveryCharges:Int,
    val phoneNumber:String,
    val email:String
)