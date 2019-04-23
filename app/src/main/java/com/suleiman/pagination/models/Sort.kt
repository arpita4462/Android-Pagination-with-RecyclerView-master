package com.suleiman.pagination.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Sort (
    var sorted: Boolean? = null,
    var unsorted: Boolean? = null,
    var empty: Boolean? = null
)