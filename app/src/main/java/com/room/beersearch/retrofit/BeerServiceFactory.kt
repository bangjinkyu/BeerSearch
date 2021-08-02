package com.room.beersearch.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BeerServiceFactory {
    companion object{
        val DOMAIN:String = "https://api.punkapi.com/"

        fun create() : BeerService {
            val retrofit = Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기 사용;
                .build()
            return retrofit.create(BeerService::class.java)
        }
    }
}