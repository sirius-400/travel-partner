package com.abc.travelpartner.data.response.route

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RouteApiService {
    @GET("directions/json")
    fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Call<RouteResponse>
}