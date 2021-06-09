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
    private var _route: MutableLiveData<RoutesItem>? = null
    var route: LiveData<RoutesItem> = _route!!

    private var _nearbyPlaces: MutableLiveData<List<NearbyPlacesResponse>>? = null
    var nearbyPlaces: LiveData<List<NearbyPlacesResponse>> = _nearbyPlaces!!

    fun getDirection(start: String, end: String, context: Context): LiveData<RoutesItem> {
        val client = RouteApiConfig.getInstance().getDirection(start,end, BuildConfig.GMP_KEY)
        client.enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                if(response.isSuccessful){
                    _route?.value = response.body()?.routes?.get(0)
                }
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
        return route
    }

    fun getAllNearbyPlaces(): LiveData<List<NearbyPlacesResponse>>{
        val client = NearbyPlaceApiConfig.getInstance().getNearbyPlaces()
        client.enqueue(object: Callback<List<NearbyPlacesResponse>>{
            override fun onResponse(call: Call<List<NearbyPlacesResponse>>, response: Response<List<NearbyPlacesResponse>>) {
                if (response.isSuccessful){
                    _nearbyPlaces?.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<NearbyPlacesResponse>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
        return nearbyPlaces
    }
}