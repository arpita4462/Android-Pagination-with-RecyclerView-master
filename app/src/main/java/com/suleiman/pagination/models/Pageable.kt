package com.suleiman.pagination.models


data class Pageable (
    var sort: Sort? = null,
    var offset: Int? = null,
    var pageSize: Int? = null,
    var pageNumber: Int? = null,
    var paged: Boolean? = null,
    var unpaged: Boolean? = null

)