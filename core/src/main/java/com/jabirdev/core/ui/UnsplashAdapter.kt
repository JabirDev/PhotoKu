package com.jabirdev.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.Target
import com.jabirdev.core.R
import com.jabirdev.core.databinding.ItemImageBinding
import com.jabirdev.core.domain.model.Unsplash

class UnsplashAdapter : PagingDataAdapter<Unsplash, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((Unsplash) -> Unit)? = null
    var onFavoriteClick: ((Unsplash, Boolean) -> Unit)? = null

    inner class UnsplashHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root){
        private val context = binding.root.context

        fun setData(data : Unsplash){
            binding.root.setOnClickListener {
                onItemClick?.invoke(data)
            }
            binding.ibFavorite.setOnClickListener {
                val isFavorite = !data.isFavorite
                onFavoriteClick?.invoke(data, isFavorite)
            }
            binding.ibFavorite.isChecked = data.isFavorite
            binding.ivThumb.layout(0,0,0,0)
            Glide.with(context)
                .load(data.thumbImage)
                .override(Target.SIZE_ORIGINAL)
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .thumbnail(0.5f)
                .into(binding.ivThumb)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        val unsplashHolder = (holder as UnsplashHolder)
        if (data != null) unsplashHolder.setData(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnsplashHolder(view)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Unsplash>() {
            override fun areItemsTheSame(oldItem: Unsplash, newItem: Unsplash): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Unsplash, newItem: Unsplash): Boolean {
                return oldItem.id == newItem.id
            }

            override fun getChangePayload(oldItem: Unsplash, newItem: Unsplash): Any? {
                return if (oldItem.isFavorite != newItem.isFavorite) true else null
            }

        }
    }

//    interface ItemClickListener {
//        fun onClick(photo: Unsplash)
//        fun onFavorite(photo: Unsplash, isFavorite: Boolean)
//    }

}