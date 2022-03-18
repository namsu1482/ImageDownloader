package com.cns.imagedownloader.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cns.imagedownloader.databinding.LayoutItemBinding
import com.cns.imagedownloader.model.SampleItem

class SampleItemAdapter : RecyclerView.Adapter<SampleItemAdapter.ItemViewHolder>() {
    var itemList = ArrayList<SampleItem>()
    lateinit var itemClickListener: (SampleItem) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ItemViewHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(itemList.get(position))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setOnItemClickListener(listener: (SampleItem) -> Unit) {
        this.itemClickListener = listener
    }

    inner class ItemViewHolder(private val binding: LayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SampleItem) {
            binding.tvItemName.text = item.title
            binding.sampleitem = item
            binding.layoutItem.setOnClickListener {
                itemClickListener(item)
            }
        }

    }

}
