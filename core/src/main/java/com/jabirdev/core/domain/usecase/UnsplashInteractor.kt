package com.jabirdev.core.domain.usecase

import androidx.paging.PagingData
import com.jabirdev.core.data.source.remote.Resource
import com.jabirdev.core.data.source.remote.response.UnsplashItem
import com.jabirdev.core.domain.model.Unsplash
import com.jabirdev.core.domain.model.UnsplashDetail
import com.jabirdev.core.domain.repository.IUnsplashRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class UnsplashInteractor @Inject constructor(
    private val unsplashRepository: IUnsplashRepository
) : UnsplashUseCase {
    override fun getPagingPhotos(orderBy: String): Flow<PagingData<Unsplash>> = unsplashRepository.getPagingPhotos(orderBy)

    override fun getFavoriteList(): Flow<PagingData<Unsplash>> = unsplashRepository.getFavoriteList()

    override fun searchPhotos(query: String): Flow<PagingData<Unsplash>> = unsplashRepository.searchPhotos(query)

    override suspend fun getDetail(id: String): Response<UnsplashDetail> = unsplashRepository.getDetail(id)

    override fun setFavorite(photo: Unsplash, isFavorite: Boolean) = unsplashRepository.setFavorite(photo, isFavorite)
}