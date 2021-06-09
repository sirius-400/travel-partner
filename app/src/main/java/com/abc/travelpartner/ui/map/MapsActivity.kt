package com.abc.travelpartner.ui.map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.abc.travelpartner.*
import com.abc.travelpartner.databinding.ActivityMapsBinding
import com.abc.travelpartner.ui.account.AccountActivity
import com.abc.travelpartner.ui.listplaces.ListPlacesActivity
import com.abc.travelpartner.utils.AddMarkersOfNearbyPlaces
import com.abc.travelpartner.utils.DrawRoute
import com.abc.travelpartner.utils.MyItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<MyItem>
    private lateinit var addMarker: AddMarkersOfNearbyPlaces
    private lateinit var viewModel: MapViewModel

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[MapViewModel::class.java]
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        clusterManager = ClusterManager(this,mMap)
        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)
        addMarker = AddMarkersOfNearbyPlaces(clusterManager)

        val magelang = LatLng(-7.479734, 110.217697)
        val borobudur = LatLng(-7.607355, 110.203804)
        val builder = LatLngBounds.builder()
        builder.include(magelang)
        builder.include(borobudur)
        val bounds = builder.build()

        mMap.setOnMapLoadedCallback(object: GoogleMap.OnMapLoadedCallback{
            override fun onMapLoaded() {
                mMap.addMarker(MarkerOptions().position(magelang).title("Marker in Magelang").icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                ))
                mMap.addMarker(MarkerOptions().position(borobudur).title("Marker in Borobudur"))
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0)
                mMap.animateCamera(cameraUpdate)
            }
        })

        getDirection(magelang,borobudur)

        getNearbyPlaces()

        binding.btnList.setOnClickListener {
            val intent = Intent(this, ListPlacesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }

    private fun getDirection(origin: LatLng, destination: LatLng) {
        val start = origin.latitude.toString() +","+ origin.longitude.toString()
        val end = destination.latitude.toString() +","+ destination.longitude.toString()
        binding.progressbar.visibility = View.VISIBLE
        viewModel.getDirection(start,end,this).observe(this,{route->
            binding.progressbar.visibility = View.GONE
            DrawRoute.drawRoute(mMap, route)
        })
    }

    fun getNearbyPlaces() {
        binding.progressbar.visibility = View.VISIBLE
        viewModel.getAllNearbyPlaces().observe(this,{result->
            binding.progressbar.visibility = View.GONE
            addMarker.addMarkers(result)
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