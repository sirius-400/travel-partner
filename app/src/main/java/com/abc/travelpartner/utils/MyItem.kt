package com.abc.travelpartner.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MyItem(
    id: String,
    lat: Double,
    lng: Double,
    title: String,
    snippet: String
): ClusterItem {
    private val position: LatLng
    private val title: String
    private val snippet: String
    private val id: String

    override fun getPosition(): LatLng = position

    override fun getTitle(): String = title

    override fun getSnippet(): String = snippet

    init {
        position = LatLng(lat, lng)
        this.title = title
        this.snippet = snippet
        this.id = id
    }
}