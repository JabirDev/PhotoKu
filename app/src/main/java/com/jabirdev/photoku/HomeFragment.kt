package com.jabirdev.photoku

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jabirdev.core.ui.UnsplashAdapter
import com.jabirdev.core.utils.ItemOffsetDecoration
import com.jabirdev.core.utils.withLoadStateAdapters
import com.jabirdev.photoku.adapter.LoadingStateAdapter
import com.jabirdev.photoku.databinding.FragmentHomeBinding
import com.jabirdev.photoku.vm.MainViewModel
import com.jabirdev.photoku.vm.OrderBy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    
    private var mainActivity: MainActivity? = null

    private val mainViewModel: MainViewModel by viewModels()
    private val unsplashAdapter: UnsplashAdapter = UnsplashAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root

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
        binding?.rvImage?.apply {
            setHasFixedSize(true)
            layoutManager = staggeredLayoutManager
            adapter = unsplashAdapter.withLoadStateAdapters(
                header = LoadingStateAdapter {
                    unsplashAdapter.retry()
                },
                footer = LoadingStateAdapter {
                    unsplashAdapter.retry()
                }
            )
            addItemDecoration(ItemOffsetDecoration(requireContext(), R.dimen.item_offset))
        }

        mainViewModel.photosList.observe(viewLifecycleOwner){
            unsplashAdapter.submitData(lifecycle, it)
        }

        initMenu()

    }

    private fun initMenu() {
        val popup = PopupMenu(requireContext(), mainActivity?.btnOrder)
        popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)
        popup.setOnMenuItemClickListener { item: MenuItem ->
            when(item.itemId){
                R.id.filter_latest -> mainViewModel.loadPhotos(OrderBy.LATEST)
                R.id.filter_popular -> mainViewModel.loadPhotos(OrderBy.POPULAR)
                R.id.filter_oldest -> mainViewModel.loadPhotos(OrderBy.OLDEST)
            }
            Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
            mainActivity?.btnOrder?.text = item.title
            return@setOnMenuItemClickListener true
        }
        mainActivity?.btnOrder?.text = popup.menu[0].title
        mainActivity?.btnOrder?.setOnClickListener { popup.show() }
    }

    override fun onStart() {
        super.onStart()
        binding?.rvImage?.adapter = unsplashAdapter
        mainViewModel.loadPhotos(OrderBy.LATEST)
        initMenu()
    }

    override fun onStop() {
        super.onStop()
        binding?.rvImage?.adapter = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}