package com.jabirdev.core.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jabirdev.core.R
import com.jabirdev.core.databinding.ItemImageBinding
import com.jabirdev.core.domain.model.Unsplash
import com.jabirdev.core.utils.loadImage

class UnsplashAdapter : PagingDataAdapter<Unsplash, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((Unsplash) -> Unit)? = null
    var onFavoriteClick: ((Unsplash, Boolean) -> Unit)? = null

    inner class UnsplashHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root){

        private val context: Context = binding.root.context

        fun setData(data : Unsplash){
            binding.root.setOnClickListener {
                onItemClick?.invoke(data)
            }
            binding.ibFavorite.setOnClickListener {
                val isFavorite = !data.isFavorite
                val msg = if (isFavorite) context.getString(R.string.added_to_favorite) else context.getString(R.string.removed_from_favorite)
                onFavoriteClick?.invoke(data, isFavorite)
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    .show()

            }
            binding.ibFavorite.isChecked = data.isFavorite
            binding.ivThumb.layout(0,0,0,0)
            binding.ivThumb.loadImage(data.thumbImage)
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

}