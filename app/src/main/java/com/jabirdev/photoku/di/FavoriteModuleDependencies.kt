package com.jabirdev.photoku.di

import com.jabirdev.core.domain.usecase.UnsplashUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependencies {

    fun unsplashUseCase(): UnsplashUseCase

}