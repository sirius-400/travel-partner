package com.abc.travelpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abc.travelpartner.databinding.ActivityDetailPlaceBinding
import com.bumptech.glide.Glide

class DetailPlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPlaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val place = intent.getParcelableExtra<Place>(EXTRA_PLACE)
        supportActionBar?.title = place?.name
        Glide.with(this)
            .load(place?.image)
            .into(binding.imgDetailPhoto)
        binding.tvDetailName.text = place?.name
        binding.tvPlaceContact.text = place?.contact
    }
    companion object{
        const val EXTRA_PLACE = "extra_place"
    }
}