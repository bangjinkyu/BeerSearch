package com.room.beersearch.retrofit

import com.room.beersearch.model.BeerSearch
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerService {
    @GET("/v2/beers")
    suspend fun getBooksName(
        @Query("beer_name") query: String,
        @Query( "page") pageNum: Int,
        @Query( "per_page") size: Int
    ): List<BeerSearch>
}