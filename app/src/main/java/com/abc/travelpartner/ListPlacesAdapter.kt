package com.abc.travelpartner

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abc.travelpartner.databinding.ItemCardviewBinding
import com.bumptech.glide.Glide

class ListPlacesAdapter: RecyclerView.Adapter<ListPlacesAdapter.ListViewHolder>() {
    private val mData = ArrayList<Place>()
    private lateinit var binding: ItemCardviewBinding
    fun setData(items: ArrayList<Place>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }
    inner class ListViewHolder (binding: ItemCardviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeItems: Place) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(placeItems.photo)
                    .into(binding.imgPhoto)
                tvName.text = placeItems.nama
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context,DetailPlaceActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlacesAdapter.ListViewHolder {
        binding = ItemCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListPlacesAdapter.ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size
}