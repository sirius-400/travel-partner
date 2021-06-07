package com.abc.travelpartner

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil

class DrawRoute {
    companion object{
        private val path : MutableList<List<LatLng>> = arrayListOf()
        fun drawRoute(googleMap: GoogleMap, route: RoutesItem?){
            route?.legs?.get(0)?.steps?.forEach({
                val point = it?.polyline?.points
                path.add(PolyUtil.decode(point))
            })
            for(i in 0 until path.size){
                googleMap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.BLUE))
            }
        }
    }
}