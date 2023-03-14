package com.jabirdev.photoku

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.chip.Chip
import com.jabirdev.core.data.source.remote.Status
import com.jabirdev.core.domain.model.Unsplash
import com.jabirdev.photoku.vm.DetailViewModel
import com.jabirdev.core.utils.countViews
import com.jabirdev.photoku.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PHOTO_ITEM, Unsplash::class.java)
        } else {
            intent.getParcelableExtra(PHOTO_ITEM)
        }

        Glide.with(this)
            .load(photoItem?.regularImage)
            .placeholder(R.drawable.image_placeholder)
            .into(binding.ivThumbDetail)

        detailViewModel.photo.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    it.data?.tags?.forEach { t ->
                        val chip = Chip(this)
                        chip.text = t.title
                        binding.chipGroupTags.addView(chip)
                    }
                    Glide.with(this)
                        .load(it.data?.user?.profileImage?.large)
                        .transform(CircleCrop())
                        .into(binding.ivAvatar)
                    binding.tvName.text = it.data?.user?.name
                    binding.btnViews.apply {
                        text = if (it.data?.views != null) countViews(it.data?.views!!.toLong()) else "0"
                        visibility = View.VISIBLE
                    }
                    binding.btnDownloads.apply {
                        text = if (it.data?.downloads != null) countViews(it.data?.downloads!!.toLong()) else "0"
                        visibility = View.VISIBLE
                    }
                    binding.btnShare.apply {
                        setOnClickListener { _ ->
                            val shareIntent = Intent.createChooser(Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, it.data?.links?.html)
                                type = "text/plain"
                            }, null)
                            startActivity(shareIntent)
                        }
                        visibility = View.VISIBLE
                    }
                    binding.btnFavorite.apply {
                        setOnClickListener { v ->
                            val newState = photoItem?.isFavorite != true
                            v.isSelected = newState
                            detailViewModel.setFavorite(photoItem!!, newState)
                        }
                        visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> Toast.makeText(this, "Loading data", Toast.LENGTH_SHORT).show()
                Status.ERROR -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        photoItem?.id?.let { detailViewModel.getDetailPhoto(it) }
        binding.btnFavorite.isSelected = photoItem?.isFavorite == true
        binding.btnBack.setOnClickListener { finish() }

    }

    companion object {
        const val PHOTO_ITEM = "photo_item"
    }

}