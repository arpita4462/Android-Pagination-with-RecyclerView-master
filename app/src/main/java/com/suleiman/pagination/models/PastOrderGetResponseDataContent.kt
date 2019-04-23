package com.suleiman.pagination.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName




data class PastOrderGetResponseDataContent(

    var customerOrderId: String? = null,
    var paymentId: Int? = null,
    var userId: Int? = null,
    var restaurantId: Int? = null,
    var cart: List<Cart>? = null,
    var userName: String? = null,
    var email: String? = null,
    var restaurantName: String? = null,
    var restaurantAddress: String? = null,
    var location: String? = null,
    var totalAmount: Double? = null,
    var totalQuatity: Int? = null,
    var status: String? = null,
    var deliveryTime: String? = null,
    var orderCartDetailId: Int? = null,
    var orderType: String? = null,
    var myboxStore: MyBoxStore? = null
)
