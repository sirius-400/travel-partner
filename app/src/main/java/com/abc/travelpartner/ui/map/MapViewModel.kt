package com.abc.travelpartner.ui.map

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abc.travelpartner.BuildConfig
import com.abc.travelpartner.data.response.nearbyplace.NearbyPlaceApiConfig
import com.abc.travelpartner.data.response.nearbyplace.NearbyPlacesResponse
import com.abc.travelpartner.data.response.route.RouteApiConfig
import com.abc.travelpartner.data.response.route.RouteResponse
import com.abc.travelpartner.data.response.route.RoutesItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel: ViewModel() {
    fun getDirection(start: String, end: String, context: Context): LiveData<RoutesItem> {
        val _route = MutableLiveData<RoutesItem>()
        val client = RouteApiConfig.getInstance().getDirection(start,end, BuildConfig.GMP_KEY)
        client.enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                if(response.isSuccessful){
                    _route.postValue(response.body()?.routes?.get(0)!!)
                }
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
        return _route
    }

    fun getAllNearbyPlaces(): LiveData<List<NearbyPlacesResponse>>{
        val _nearbyPlaces = MutableLiveData<List<NearbyPlacesResponse>>()
        val client = NearbyPlaceApiConfig.getInstance().getNearbyPlaces()
        client.enqueue(object: Callback<List<NearbyPlacesResponse>>{
            override fun onResponse(call: Call<List<NearbyPlacesResponse>>, response: Response<List<NearbyPlacesResponse>>) {
                if (response.isSuccessful){
                    _nearbyPlaces.postValue(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<NearbyPlacesResponse>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
        return _nearbyPlaces
    }
}