package com.jabirdev.core.di

import com.jabirdev.core.data.UnsplashRepository
import com.jabirdev.core.domain.repository.IUnsplashRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(unsplashRepository: UnsplashRepository): IUnsplashRepository

}