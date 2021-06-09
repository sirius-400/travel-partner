package com.abc.travelpartner.data.response.nearbyplace

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NearbyPlaceApiConfig {
    companion object{
        fun getInstance(): NearbyPlaceApiService {
            val logginInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                    .addInterceptor(logginInterceptor)
                    .build()
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://asia-southeast2-bangkit-capstone-400.cloudfunctions.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(NearbyPlaceApiService::class.java)
        }
    }
}