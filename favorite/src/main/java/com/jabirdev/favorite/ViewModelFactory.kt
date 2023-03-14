package com.jabirdev.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jabirdev.core.domain.usecase.UnsplashUseCase
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val unsplashUseCase: UnsplashUseCase
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> FavoriteViewModel(unsplashUseCase) as T
            else -> throw Throwable("Unknown viewModel class: ${modelClass.name}")
        }

}