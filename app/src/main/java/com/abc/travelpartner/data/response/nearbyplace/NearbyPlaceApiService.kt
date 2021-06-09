package com.abc.travelpartner.data.response.nearbyplace

import retrofit2.Call
import retrofit2.http.GET

interface NearbyPlaceApiService {
    @GET("models")
    fun getNearbyPlaces(): Call<List<NearbyPlacesResponse>>
}