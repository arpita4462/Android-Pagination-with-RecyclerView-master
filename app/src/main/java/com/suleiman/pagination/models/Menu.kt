package com.suleiman.pagination.models


data class Menu (
    var foodItemId: Int? = null,
    var restaurant: Restaurant? = null,
    var foodType: String? = null,
    var foodItemName: String? = null,
    var foodItemPrice: Double? = null,
    var foodItemDescription: String? = null,
    var foodImage: String? = null,
    var currency: String? = null,
    var foodItemStatus: String? = null,
    var foodRating: String? = null,
    var quantity: Int? = null
)


