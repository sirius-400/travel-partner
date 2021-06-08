package com.abc.travelpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.abc.travelpartner.databinding.ActivityListPlacesBinding
import retrofit2.Call
import retrofit2.Callback

class ListPlacesActivity : AppCompatActivity() {
    companion object {
        const val LOCATION_EXTRA = "location_extra"
    }
    private lateinit var binding: ActivityListPlacesBinding
    private lateinit var adapter: ListPlacesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ListPlacesAdapter()
        val listLocations = intent.getStringArrayListExtra(LOCATION_EXTRA)
        listLocations?.forEach { location ->
            getNearbyPlaces(location, "restaurant")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_places_menu,menu)
        val search = menu?.findItem(R.id.search_icon)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "type here to search"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        return true
    }

    fun getNearbyPlaces(location: String, type: String) {
        var list: ArrayList<Place> = arrayListOf()
        val client = ApiConfig.getInstance().getNearbyPlaces(location,"500",type,BuildConfig.GMP_KEY)
        client.enqueue(object : Callback<NearbyPlacesResponse> {
            override fun onResponse(call: Call<NearbyPlacesResponse>, response: retrofit2.Response<NearbyPlacesResponse>) {

                val nearbyPlaces = response.body()?.results as List<ResultsItem>
                nearbyPlaces.forEach {item ->
                    list.add(Place(item.name.toString(),"https://assets-a1.kompasiana.com/items/album/2020/10/12/mcdonalds-5f8468188ede48780604a122.jpg?t=o&v=410"))
                }
                binding.rvHero.setHasFixedSize(true)
                adapter.setData(list)
                binding.rvHero.layoutManager = LinearLayoutManager(applicationContext)
                binding.rvHero.adapter = adapter
            }

            override fun onFailure(call: Call<NearbyPlacesResponse>, t: Throwable) {
                Toast.makeText(this@ListPlacesActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}