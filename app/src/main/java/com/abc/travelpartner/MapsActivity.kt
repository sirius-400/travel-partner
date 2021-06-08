package com.abc.travelpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
    private var listLocations: ArrayList<String> = arrayListOf()

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

        binding.fabFood.setOnClickListener {
            listLocations.forEach { location ->
                getNearbyPlaces(location,"restoran")
            }
        }

        binding.fabStore.setOnClickListener {
            listLocations.forEach { location ->
                getNearbyPlaces(location,"store")
            }
        }
    }

    private fun addToListLocations(listSteps: List<StepsItem?>) {
        val center = (listSteps.size / 2).toInt()
        val start = (center / 2).toInt()
        val end = listSteps.size - start

        listLocations.let {
            it.add(listSteps[start]?.endLocation?.lat?.toString() + "," + listSteps[start]?.endLocation?.lng?.toString())
            it.add(listSteps[center]?.endLocation?.lat?.toString() + "," + listSteps[center]?.endLocation?.lng?.toString())
            it.add(listSteps[end]?.endLocation?.lat?.toString() + "," + listSteps[end]?.endLocation?.lng?.toString())
        }
    }

    private fun getDirection(origin: LatLng, destination: LatLng) {
        val start = origin.latitude.toString() +","+ origin.longitude.toString()
        val end = destination.latitude.toString() +","+ destination.longitude.toString()
        val client = ApiConfig.getInstance().getDirection(start,end,BuildConfig.GMP_KEY)
        client.enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                if(response.isSuccessful){
                    val route = response.body()?.routes?.get(0)
                    val steps = route?.legs?.get(0)?.steps
                    addToListLocations(steps!!)
                    DrawRoute.drawRoute(mMap,route)
                }
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getNearbyPlaces(location: String, type: String) {
        val client = ApiConfig.getInstance().getNearbyPlaces(location,"500",type,BuildConfig.GMP_KEY)
        client.enqueue(object : Callback<NearbyPlacesResponse>{
            override fun onResponse(call: Call<NearbyPlacesResponse>, response: retrofit2.Response<NearbyPlacesResponse>) {
                val nearbyPlaces = response.body()?.results as List<ResultsItem>
                addMarker.addMarkers(nearbyPlaces)
            }

            override fun onFailure(call: Call<NearbyPlacesResponse>, t: Throwable) {
                Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
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