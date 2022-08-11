package com.cns.imagedownloader.view.search

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cns.imagedownloader.databinding.LayoutImgBinding
import com.cns.imagedownloader.model.ImageItemInfo

class ImgListAdapter : RecyclerView.Adapter<ImgListAdapter.ImgItemHolder>() {
    var imgList = ArrayList<ImageItemInfo>()
    lateinit var imgClickListener: (ImageItemInfo) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImgListAdapter.ImgItemHolder {
        val binding =
            LayoutImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ImgItemHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: ImgItemHolder, position: Int) {
        holder.bind(imgList.get(position))
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    fun setItemClickListener(listener: (ImageItemInfo) -> Unit) {
        this.imgClickListener = listener
    }

    inner class ImgItemHolder(private val binding: LayoutImgBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageItemInfo) {
            binding.imgItem = item
            binding.progressVisibility = true
            Glide.with(binding.root.context)
                .load(item.smallUrl)
                .override(300, 300)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressVisibility = false
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressVisibility = false
                        return false
                    }
                })
                .into(binding.imgSearch)

            binding.containerImg.setOnClickListener {
                imgClickListener(item)
            }

        }

    }
}