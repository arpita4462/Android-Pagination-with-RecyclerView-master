package com.suleiman.pagination.api


import com.suleiman.pagination.models.PastOrderGetResponse
import com.suleiman.pagination.models.TopRatedMovies

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Pagination
 * Created by Suleiman19 on 10/27/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */

interface MovieService {

    @GET("movie/top_rated")
    fun getTopRatedMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") pageIndex: Int
    ): Call<TopRatedMovies>

    @GET("/customerOrderDetail/get/user/{id}/statuses/public")
    fun getCustomerOderDetail(@Path("id") id: Int, @Query("statuses") statuses: String,  @Query("page") pageIndex: Int,  @Query("size") sizeIndex: Int): Call<PastOrderGetResponse>
}
