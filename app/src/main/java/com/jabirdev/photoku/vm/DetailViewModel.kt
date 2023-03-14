package com.jabirdev.photoku.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jabirdev.core.data.source.remote.Resource
import com.jabirdev.core.domain.model.Unsplash
import com.jabirdev.core.domain.model.UnsplashDetail
import com.jabirdev.core.domain.usecase.UnsplashUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val unsplashUseCase: UnsplashUseCase
) : ViewModel() {

    private val _photo = MutableLiveData<Resource<UnsplashDetail>>()
    val photo: LiveData<Resource<UnsplashDetail>> get() = _photo

    fun getDetailPhoto(id: String) = viewModelScope.launch {
        _photo.postValue(Resource.loading(null))
        unsplashUseCase.getDetail(id).let {
            if (it.isSuccessful){
                _photo.postValue(Resource.success(it.body()))
            } else {
                _photo.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

    fun setFavorite(photo: Unsplash, isFavorite: Boolean) = viewModelScope.launch(Dispatchers.IO){
        unsplashUseCase.setFavorite(photo, isFavorite)
    }

}
