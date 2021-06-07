package com.abc.travelpartner

import com.google.maps.android.clustering.ClusterManager

class AddMarkersOfNearbyPlaces(val clusterManager: ClusterManager<MyItem>) {
    private val listMarker: MutableList<MyItem> = arrayListOf()
    fun addMarkers(results: List<ResultsItem>) {
        removeMarker()
        results.forEach {resultItem->
            val lat = resultItem.geometry?.location?.lat
            val lng = resultItem.geometry?.location?.lng
            val placeName = resultItem.name
            val item = MyItem(lat!!,lng!!,placeName!!,"this is snippet")
            listMarker.add(item)
        }
        clusterManager.addItems(listMarker)
        clusterManager.cluster()
    }
    fun removeMarker() {
        clusterManager.clearItems()
        clusterManager.cluster()
    }
}