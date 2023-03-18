package com.jabirdev.photoku.vm

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jabirdev.core.domain.model.Unsplash
import com.jabirdev.core.domain.usecase.UnsplashUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val unsplashUseCase: UnsplashUseCase
) : ViewModel() {

    private val _query = MutableLiveData<String>()

    val searchResult: LiveData<PagingData<Unsplash>> = _query.switchMap { query ->
        unsplashUseCase.searchPhotos(query).asLiveData(Dispatchers.IO).cachedIn(viewModelScope)
    }

    fun search(query: String): Unit = _query.postValue(query)

    fun setFavorite(photo: Unsplash, isFavorite: Boolean): Job = viewModelScope.launch(Dispatchers.IO){
        unsplashUseCase.setFavorite(photo, isFavorite)
    }

}