package com.jabirdev.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jabirdev.core.domain.model.Unsplash
import com.jabirdev.core.domain.usecase.UnsplashUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val unsplashUseCase: UnsplashUseCase
) : ViewModel() {

    val favoriteList: LiveData<PagingData<Unsplash>> =
        unsplashUseCase.getFavoriteList().asLiveData(Dispatchers.IO).cachedIn(viewModelScope)

    fun setFavorite(photo: Unsplash, isFavorite: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        unsplashUseCase.setFavorite(photo, isFavorite)
    }

}