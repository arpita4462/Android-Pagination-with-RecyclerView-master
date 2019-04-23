package com.suleiman.pagination.models


data class PastOrderGetResponseData(

    var content: List<PastOrderGetResponseDataContent>? = null,
    var pageable: Pageable? = null,
    var totalPages: Int? = null,
    var totalElements: Int? = null,
    var last: Boolean? = null,
    var size: Int? = null,
    var numberOfElements: Int? = null,
    var first: Boolean? = null,
    var sort: Sort? = null,
    var number: Int? = null,
    var empty: Boolean? = null
)

