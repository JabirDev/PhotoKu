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
class MainViewModel @Inject constructor(
    private val unsplashUseCase: UnsplashUseCase
) : ViewModel() {

    private val _orderBy = MutableLiveData<String>()
    val photosList: LiveData<PagingData<Unsplash>> = _orderBy.switchMap { orderBy ->
        unsplashUseCase.getPagingPhotos(orderBy).asLiveData(Dispatchers.IO).cachedIn(viewModelScope)
    }

    init {
        loadPhotos(OrderBy.LATEST)
    }

    fun setFavorite(photo: Unsplash, isFavorite: Boolean): Job = viewModelScope.launch(Dispatchers.IO){
        unsplashUseCase.setFavorite(photo, isFavorite)
    }

    fun loadPhotos(orderBy: String): Unit = _orderBy.postValue(orderBy)


}
object OrderBy {
    const val LATEST = "latest"
    const val OLDEST = "oldest"
    const val POPULAR = "popular"
}
