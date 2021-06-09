package com.abc.travelpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.abc.travelpartner.databinding.ActivityListPlacesBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ListPlacesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPlacesBinding

    private lateinit var adapter: ListPlacesAdapter
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var placeCollection: CollectionReference = db.collection("Places")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ListPlacesAdapter()
        binding.rvHero.setHasFixedSize(true)
        getListPlaces()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_places_menu,menu)
        val search = menu?.findItem(R.id.search_icon)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "type here to search"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })
        return true
    }

    fun getListPlaces() {
        val list: ArrayList<Place> = arrayListOf()
        binding.progressbar.visibility = View.VISIBLE
        placeCollection.get()
                .addOnSuccessListener {
                    binding.progressbar.visibility = View.GONE
                    for (document in it.documents) {
                        val place = document.toObject(Place::class.java)
                        list.add(place!!)
                    }
                    showRecylerList(list)
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
    }

    private fun showRecylerList(list: ArrayList<Place>) {
        adapter.setData(list)
        binding.rvHero.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvHero.adapter = adapter

        adapter.setOnItemClickCallback(object: ListPlacesAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Place) {
                val visitorMap = mutableMapOf<String,Long>()
                placeCollection.whereEqualTo("place_id",data.place_id).get()
                        .addOnSuccessListener {querySnapshot ->
                            for (document in querySnapshot.documents) {
                                val visitor = document.getLong("visitor")
                                visitorMap["visitor"] = visitor?.inc()!!
                                placeCollection.document(document.id).set(
                                        visitorMap, SetOptions.merge())
                            }
                            val intent = Intent(this@ListPlacesActivity,DetailPlaceActivity::class.java)
                            intent.putExtra(DetailPlaceActivity.EXTRA_PLACE,data)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@ListPlacesActivity,it.message,Toast.LENGTH_SHORT)
                                    .show()
                        }
            }

        })
    }
}