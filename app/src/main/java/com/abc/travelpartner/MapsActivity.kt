package com.abc.travelpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val amanjiwo = LatLng(-7.632857, 110.200882)
        val borobudur = LatLng(-7.637170, 110.204041)
        mMap.addMarker(MarkerOptions().position(amanjiwo).title("Marker in Amanjiwo"))
        mMap.addMarker(MarkerOptions().position(borobudur).title("Marker in Borobudur"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(amanjiwo, 20.5f))

        DrawRoute.drawRoute(mMap,getDirection(amanjiwo,borobudur))
    }

    fun getDirection(origin: LatLng, destination: LatLng): RoutesItem {
        lateinit var route: RoutesItem
        val start = origin.latitude.toString() +","+ origin.longitude.toString()
        val end = destination.latitude.toString() +","+ destination.longitude.toString()
        val client = ApiConfig.getInstance().getDirection(start,end,BuildConfig.GMP_KEY)
        client.enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                route = response.body()?.routes?.get(0)!!
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
        return route
    }
}