package com.jabirdev.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jabirdev.core.ui.LoadingStateAdapter
import com.jabirdev.photoku.vm.MainViewModel
import com.jabirdev.core.ui.UnsplashAdapter
import com.jabirdev.core.utils.ItemOffsetDecoration
import com.jabirdev.favorite.databinding.ActivityFavoriteBinding
import com.jabirdev.photoku.DetailActivity
import com.jabirdev.photoku.R
import com.jabirdev.photoku.di.FavoriteModuleDependencies
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    @Inject
    lateinit var factory: ViewModelFactory

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        factory
    }

    private val unsplashAdapter = UnsplashAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerFavoriteComponent.builder()
            .context(this)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext, FavoriteModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        unsplashAdapter.onItemClick = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.PHOTO_ITEM, it)
            startActivity(intent)
        }
        unsplashAdapter.onFavoriteClick = { photo, isFavorite ->
            favoriteViewModel.setFavorite(photo, isFavorite)
            unsplashAdapter.refresh()
        }

        val staggeredLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = staggeredLayoutManager
            adapter = unsplashAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    unsplashAdapter.retry()
                }
            )
            addItemDecoration(ItemOffsetDecoration(this@FavoriteActivity, R.dimen.item_offset))
        }

        favoriteViewModel.favoriteList.observe(this@FavoriteActivity){
            unsplashAdapter.submitData(lifecycle, it)
        }

    }

    override fun onStart() {
        super.onStart()
        unsplashAdapter.refresh()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

}