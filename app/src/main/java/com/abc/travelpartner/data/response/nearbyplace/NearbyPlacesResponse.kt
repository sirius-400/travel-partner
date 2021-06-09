package com.abc.travelpartner.data.response.nearbyplace

import com.google.gson.annotations.SerializedName

data class NearbyPlacesResponse(

        @field:SerializedName("name")
    val name: String? = null,

        @field:SerializedName("place_id")
    val placeId: String? = null,

        @field:SerializedName("location")
    val location: Location? = null,

        @field:SerializedName("forecast")
    val forecast: List<Double?>? = null
)

data class Location(
     @field:SerializedName("lat")
     val lat: Double? = null,

     @field:SerializedName("lng")
     val lng: Double? = null,
)
