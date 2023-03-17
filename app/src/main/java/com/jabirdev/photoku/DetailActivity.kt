package com.jabirdev.photoku

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.chip.Chip
import com.jabirdev.core.data.source.remote.Status
import com.jabirdev.core.domain.model.Unsplash
import com.jabirdev.core.utils.countViews
import com.jabirdev.core.utils.isNetworkConnected
import com.jabirdev.photoku.databinding.ActivityDetailBinding
import com.jabirdev.photoku.util.parcelable
import com.jabirdev.photoku.vm.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val photoItem = intent.parcelable<Unsplash>(PHOTO_ITEM)

        binding?.ivThumbDetail?.let {
            Glide.with(this)
                .load(photoItem?.regularImage)
                .placeholder(R.drawable.image_placeholder)
                .into(it)
        }

        detailViewModel.photo.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    binding?.loading?.lottieLoading?.visibility = View.GONE
                    it.data?.tags?.forEach { t ->
                        val chip = Chip(this)
                        chip.text = t.title
                        binding?.chipGroupTags?.addView(chip)
                    }
                    binding?.ivAvatar?.let { it1 ->
                        Glide.with(this)
                            .load(it.data?.user?.profileImage?.large)
                            .transform(CircleCrop())
                            .into(it1)
                    }
                    binding?.tvName?.text = it.data?.user?.name
                    binding?.btnViews?.apply {
                        text = if (it.data?.views != null) countViews(it.data?.views!!.toLong()) else "0"
                        visibility = View.VISIBLE
                    }
                    binding?.btnDownloads?.apply {
                        text = if (it.data?.downloads != null) countViews(it.data?.downloads!!.toLong()) else "0"
                        visibility = View.VISIBLE
                    }
                    binding?.btnShare?.apply {
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
                    binding?.btnFavorite?.apply {
                        setOnClickListener { v ->
                            val newState = photoItem?.isFavorite != true
                            v.isSelected = newState
                            detailViewModel.setFavorite(photoItem!!, newState)
                        }
                        visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> binding?.loading?.lottieLoading?.visibility = View.VISIBLE
                Status.ERROR -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        photoItem?.id?.let {
            if (isNetworkConnected(this)){
                detailViewModel.getDetailPhoto(it)
            } else {
                Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show()
            }
        }
        binding?.btnFavorite?.isSelected = photoItem?.isFavorite == true
        binding?.btnBack?.setOnClickListener { finish() }

    }

    companion object {
        const val PHOTO_ITEM = "photo_item"
    }

}