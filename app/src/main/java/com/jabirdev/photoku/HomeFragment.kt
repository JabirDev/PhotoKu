package com.jabirdev.photoku

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jabirdev.core.ui.LoadingStateAdapter
import com.jabirdev.core.ui.UnsplashAdapter
import com.jabirdev.core.utils.ItemOffsetDecoration
import com.jabirdev.photoku.databinding.FragmentHomeBinding
import com.jabirdev.photoku.vm.MainViewModel
import com.jabirdev.photoku.vm.OrderBy
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    
    private var mainActivity: MainActivity? = null

    private val mainViewModel: MainViewModel by viewModels()
    private val unsplashAdapter = UnsplashAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity

        unsplashAdapter.onItemClick = {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.PHOTO_ITEM, it)
            startActivity(intent)
        }
        unsplashAdapter.onFavoriteClick = { photo, isFavorite ->
            mainViewModel.setFavorite(photo, isFavorite)
        }
        val staggeredLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvImage.apply {
            setHasFixedSize(true)
            layoutManager = staggeredLayoutManager
            adapter = unsplashAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    unsplashAdapter.retry()
                }
            )
            addItemDecoration(ItemOffsetDecoration(requireContext(), R.dimen.item_offset))
        }

        mainViewModel.photosList.observe(viewLifecycleOwner){
            unsplashAdapter.submitData(lifecycle, it)
        }

        mainActivity?.btnOrder?.setOnClickListener { v ->
            showMenu(v, R.menu.menu_popup)
        }

    }

    private fun showMenu(v: View, menuPopup: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuPopup, popup.menu)
        popup.setOnMenuItemClickListener { item: MenuItem ->
            when(item.itemId){
                R.id.filter_latest -> mainViewModel.loadPhotos(OrderBy.LATEST)
                R.id.filter_popular -> mainViewModel.loadPhotos(OrderBy.POPULAR)
                R.id.filter_oldest -> mainViewModel.loadPhotos(OrderBy.OLDEST)
            }
            Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.loadPhotos(OrderBy.LATEST)
    }

}