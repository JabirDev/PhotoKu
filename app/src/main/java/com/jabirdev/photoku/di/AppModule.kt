package com.jabirdev.photoku.di

import com.jabirdev.core.domain.usecase.UnsplashInteractor
import com.jabirdev.core.domain.usecase.UnsplashUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideTourismUseCase(unsplashInteractor: UnsplashInteractor): UnsplashUseCase

}