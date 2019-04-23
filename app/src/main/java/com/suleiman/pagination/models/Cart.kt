package com.suleiman.pagination.models


data class Cart(
    var cartId: Int? = null,
    var price: Double? = null,
    var quantity: Int? = null,
    var menu: Menu? = null,
    var orderCartDate: String? = null

)

