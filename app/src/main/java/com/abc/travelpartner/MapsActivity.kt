package com.abc.travelpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<MyItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        clusterManager = ClusterManager(this,mMap)

        val amanjiwo = LatLng(-7.632857, 110.200882)
        val borobudur = LatLng(-7.607355, 110.203804)
        val builder = LatLngBounds.builder()
        builder.include(amanjiwo)
        builder.include(borobudur)
        val bounds = builder.build()

        mMap.addMarker(MarkerOptions().position(amanjiwo).title("Marker in Amanjiwo"))
        mMap.addMarker(MarkerOptions().position(borobudur).title("Marker in Borobudur"))
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0)
        mMap.animateCamera(cameraUpdate)

        getDirection(amanjiwo,borobudur)
    }

    private fun getDirection(origin: LatLng, destination: LatLng) {
        val start = origin.latitude.toString() +","+ origin.longitude.toString()
        val end = destination.latitude.toString() +","+ destination.longitude.toString()
        val client = ApiConfig.getInstance().getDirection(start,end,BuildConfig.GMP_KEY)
        client.enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                if(response.isSuccessful){
                    val route = response.body()?.routes?.get(0)
                    DrawRoute.drawRoute(mMap,route)
                }
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getNearbyPlaces(location: String, type: String) {
        val client = ApiConfig.getInstance().getNearbyPlaces(location,"500",type,"AIzaSyDs_E55Xkzhs1WkBgFL_50YOZ8ouzKXRuA")
        client.enqueue(object : Callback<NearbyPlacesResponse>{
            override fun onResponse(call: Call<NearbyPlacesResponse>, response: retrofit2.Response<NearbyPlacesResponse>) {
                val nearbyPlaces = response.body()?.results as List<ResultsItem>
                val addMarker = AddMarkersOfNearbyPlaces(clusterManager)
                addMarker.addMarkers(nearbyPlaces)
            }

            override fun onFailure(call: Call<NearbyPlacesResponse>, t: Throwable) {
                Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}