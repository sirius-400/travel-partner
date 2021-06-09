package com.abc.travelpartner

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.abc.travelpartner.databinding.ItemCardviewBinding
import com.bumptech.glide.Glide

class ListPlacesAdapter: RecyclerView.Adapter<ListPlacesAdapter.ListViewHolder>(),Filterable {
    private var mData = ArrayList<Place>()
    private val mDataFilter = ArrayList<Place>()
    private lateinit var binding: ItemCardviewBinding
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<Place>){
        mData.clear()
        mDataFilter.clear()
        mData.addAll(items)
        mDataFilter.addAll(items)
        notifyDataSetChanged()
    }
    inner class ListViewHolder (binding: ItemCardviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeItems: Place) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(placeItems.image)
                    .into(binding.imgPhoto)
                tvName.text = placeItems.name
            }
            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(placeItems)
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

    interface OnItemClickCallback {
        fun onItemClicked(data: Place)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint?.toString()?.toLowerCase()
                var filterResults = FilterResults()
                var list = ArrayList<Place>()
                if(charSearch?.isNotEmpty()!!){
                    for(item in mDataFilter){
                        if(item.name.toLowerCase().contains(charSearch)){
                            list.add(item)
                        }
                    }
                    filterResults.count = list.size
                    filterResults.values = list
                }else{
                    filterResults.count = mDataFilter.size
                    filterResults.values = mDataFilter
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mData = results?.values as ArrayList<Place>
                notifyDataSetChanged()
            }

        }
    }
}