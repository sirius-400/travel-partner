package com.abc.travelpartner

import com.google.maps.android.clustering.ClusterManager

class AddMarkersOfNearbyPlaces(val clusterManager: ClusterManager<MyItem>) {
    private val listMarker: MutableList<MyItem> = arrayListOf()
    fun addMarkers(results: List<NearbyPlacesResponse>?) {
        removeMarker()
        results?.forEach {resultItem->
            val lat = resultItem.location?.lat
            val lng = resultItem.location?.lng
            val placeName = resultItem.name
            val placeId = resultItem.placeId
            val item = MyItem(placeId.toString(),lat!!,lng!!,placeName!!,"this is snippet")
            listMarker.add(item)
        }
        clusterManager.addItems(listMarker)
        clusterManager.cluster()
    }
    fun removeMarker() {
        clusterManager.clearItems()
        listMarker.clear()
        clusterManager.cluster()
    }
}