package com.abc.travelpartner.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    var id: String = "",
    var contact: String = "",
    var name: String = "",
    var place_id: String = "",
    var image: String = ""
): Parcelable

