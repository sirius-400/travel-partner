package com.abc.travelpartner.ui.listplaces

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.abc.travelpartner.data.entity.Place
import com.abc.travelpartner.databinding.ItemCardviewBinding
import com.bumptech.glide.Glide

class ListPlacesAdapter: RecyclerView.Adapter<ListPlacesAdapter.ListViewHolder>(),Filterable {
    private var mData = ArrayList<Place>()
    private lateinit var binding: ItemCardviewBinding
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<Place>){
        mData.clear()
        mData.addAll(items)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ItemCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
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
                val filterResults = FilterResults()
                val list = ArrayList<Place>()
                if(charSearch?.isNotEmpty()!!){
                    for(item in mData){
                        if(item.name.toLowerCase().contains(charSearch)){
                            list.add(item)
                        }
                    }
                    filterResults.count = list.size
                    filterResults.values = list
                }else{
                    filterResults.count = mData.size
                    filterResults.values = mData
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