package com.abc.travelpartner.ui.listplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abc.travelpartner.ui.detailplace.DetailPlaceActivity
import com.abc.travelpartner.data.entity.Place
import com.abc.travelpartner.R
import com.abc.travelpartner.databinding.ActivityListPlacesBinding

class ListPlacesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPlacesBinding

    private lateinit var adapter: ListPlacesAdapter
    private lateinit var viewModel: ListPlacesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ListPlacesAdapter()
        binding.rvHero.setHasFixedSize(true)
        viewModel = ViewModelProvider(this).get(ListPlacesViewModel::class.java)
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

    private fun getListPlaces() {
        binding.progressbar.visibility = View.VISIBLE
        viewModel.getListPlaces(this).observe(this,{list->
            binding.progressbar.visibility = View.GONE
            showRecylerList(list)
        })
    }

    private fun showRecylerList(list: ArrayList<Place>) {
        adapter.setData(list)
        binding.rvHero.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvHero.adapter = adapter

        adapter.setOnItemClickCallback(object: ListPlacesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Place) {
                viewModel.incrementVisitors(data,this@ListPlacesActivity)
                    .observe(this@ListPlacesActivity,{place ->
                        val intent = Intent(this@ListPlacesActivity, DetailPlaceActivity::class.java)
                        intent.putExtra(DetailPlaceActivity.EXTRA_PLACE,place)
                        startActivity(intent)
                    })
            }

        })
    }
}