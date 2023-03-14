package com.jabirdev.photoku

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jabirdev.core.ui.LoadingStateAdapter
import com.jabirdev.core.ui.UnsplashAdapter
import com.jabirdev.core.utils.ItemOffsetDecoration
import com.jabirdev.photoku.databinding.FragmentSearchBinding
import com.jabirdev.photoku.vm.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val unsplashAdapter = UnsplashAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        unsplashAdapter.onItemClick = {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.PHOTO_ITEM, it)
            startActivity(intent)
        }
        unsplashAdapter.onFavoriteClick = { photo, isFavorite ->
            searchViewModel.setFavorite(photo, isFavorite)
        }

        val staggeredLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvSearch.apply {
            setHasFixedSize(true)
            layoutManager = staggeredLayoutManager
            adapter = unsplashAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    unsplashAdapter.retry()
                }
            )
            addItemDecoration(ItemOffsetDecoration(requireContext(), R.dimen.item_offset))
        }

        searchViewModel.searchResult.observe(viewLifecycleOwner){
            unsplashAdapter.submitData(lifecycle, it)
        }

        binding.inputQuery.editText?.addTextChangedListener {
            searchViewModel.search(it.toString())
        }

    }

}