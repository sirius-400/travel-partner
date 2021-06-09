package com.abc.travelpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.abc.travelpartner.databinding.ActivityMapsBinding
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
    private lateinit var addMarker: AddMarkersOfNearbyPlaces

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        clusterManager = ClusterManager(this,mMap)
        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)
        addMarker = AddMarkersOfNearbyPlaces(clusterManager)

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

        getNearbyPlaces()

        binding.btnList.setOnClickListener {
            val intent = Intent(this, ListPlacesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getDirection(origin: LatLng, destination: LatLng) {
        val start = origin.latitude.toString() +","+ origin.longitude.toString()
        val end = destination.latitude.toString() +","+ destination.longitude.toString()
        binding.progressbar.visibility = View.VISIBLE
        val client = ApiConfig.getInstance().getDirection(start,end,BuildConfig.GMP_KEY)
        client.enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                if(response.isSuccessful){
                    binding.progressbar.visibility = View.GONE
                    val route = response.body()?.routes?.get(0)
                    DrawRoute.drawRoute(mMap,route)
                }
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getNearbyPlaces() {
        binding.progressbar.visibility = View.VISIBLE
        val client = PlaceApiConfig.getInstance().getDirection()
        client.enqueue(object: Callback<List<NearbyPlacesResponse>>{
            override fun onResponse(call: Call<List<NearbyPlacesResponse>>, response: Response<List<NearbyPlacesResponse>>) {
                if (response.isSuccessful){
                    binding.progressbar.visibility = View.GONE
                    val result = response.body()
                    addMarker.addMarkers(result)
                }
            }

            override fun onFailure(call: Call<List<NearbyPlacesResponse>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.account_icon -> {
                val intent = Intent(this, AccountActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}