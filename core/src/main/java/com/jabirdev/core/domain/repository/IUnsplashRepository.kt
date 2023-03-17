package com.jabirdev.core.domain.repository

import androidx.paging.PagingData
import com.jabirdev.core.domain.model.Unsplash
import com.jabirdev.core.domain.model.UnsplashDetail
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IUnsplashRepository {

    fun getPagingPhotos(orderBy: String): Flow<PagingData<Unsplash>>

    fun getFavoriteList() : Flow<PagingData<Unsplash>>

    fun searchPhotos(query: String) : Flow<PagingData<Unsplash>>

    suspend fun getDetail(id: String): Response<UnsplashDetail>

    fun setFavorite(photo: Unsplash, isFavorite: Boolean)
}