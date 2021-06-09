package com.abc.travelpartner.ui.listplaces

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abc.travelpartner.data.entity.Place
import com.abc.travelpartner.ui.detailplace.DetailPlaceActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ListPlacesViewModel: ViewModel() {
    private var _listPlace: MutableLiveData<ArrayList<Place>>? = null
    var listPlace: LiveData<ArrayList<Place>> = _listPlace!!
    private var _place: MutableLiveData<Place>? = null
    var place: LiveData<Place> = _place!!

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var placeCollection: CollectionReference = db.collection("Places")

    fun getListPlaces(context: Context): LiveData<ArrayList<Place>> {
        val list = ArrayList<Place>()
        placeCollection.get()
            .addOnSuccessListener {
                for (document in it.documents) {
                    val place = document.toObject(Place::class.java)
                    list.add(place!!)
                }
                _listPlace?.value = list
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        return listPlace
    }

    fun incrementVisitors(data: Place, context: Context): LiveData<Place>{
        val visitorMap = mutableMapOf<String,Long>()
        placeCollection.whereEqualTo("place_id",data.place_id).get()
            .addOnSuccessListener {querySnapshot ->
                for (document in querySnapshot.documents) {
                    val visitor = document.getLong("visitor")
                    visitorMap["visitor"] = visitor?.inc()!!
                    placeCollection.document(document.id).set(
                        visitorMap, SetOptions.merge())
                }
            }
            .addOnFailureListener {
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT)
                    .show()
            }
        return place
    }
}