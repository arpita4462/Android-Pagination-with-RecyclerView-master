package com.suleiman.pagination.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class MyBoxStore (
    var myBoxStoreId: Int? = null,
    var myBoxStoreName: String? = null,
    var myBoxStorePhoneNum: String? = null,
    var myBoxStoreAddress: String? = null,
    var myBoxStoreLocation: String? = null,
    var myBoxStoreCreationDate: String? = null
)