package com.suleiman.pagination.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class PastOrderGetResponse(
    var data: PastOrderGetResponseData? = null,
    var message: String? = null
)