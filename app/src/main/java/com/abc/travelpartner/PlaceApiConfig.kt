package com.abc.travelpartner

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaceApiConfig {
    companion object{
        fun getInstance(): PlaceApiService {
            val logginInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                    .addInterceptor(logginInterceptor)
                    .build()
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://asia-southes2-bangkit-capstone-400.cloudfunctions.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(PlaceApiService::class.java)
        }
    }
}