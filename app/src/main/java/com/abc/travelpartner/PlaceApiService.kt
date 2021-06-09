package com.abc.travelpartner

import retrofit2.Call
import retrofit2.http.GET

interface PlaceApiService {
    @GET("models")
    fun getDirection(): Call<List<NearbyPlacesResponse>>
}