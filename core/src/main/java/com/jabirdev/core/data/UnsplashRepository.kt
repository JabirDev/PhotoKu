package com.jabirdev.core.data

import androidx.paging.*
import com.jabirdev.core.api.ApiService
import com.jabirdev.core.data.source.local.room.UnsplashDatabase
import com.jabirdev.core.data.source.remote.DetailApiServiceImpl
import com.jabirdev.core.data.source.remote.SearchRemoteMediator
import com.jabirdev.core.data.source.remote.UnsplashRemoteMediator
import com.jabirdev.core.domain.model.Unsplash
import com.jabirdev.core.domain.model.UnsplashDetail
import com.jabirdev.core.domain.repository.IUnsplashRepository
import com.jabirdev.core.utils.DataMapper.toDomain
import com.jabirdev.core.utils.DataMapper.toEntity
import com.jabirdev.core.utils.DataMapper.toFavoriteEntity
import com.jabirdev.core.utils.DataMapper.toResponseDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(
    private val unsplashDatabase: UnsplashDatabase,
    private val apiService: ApiService
) : IUnsplashRepository {

    private val pageSize = 30
    private val prefetchDist = (4 * pageSize)
    private val initialLoadSize = (3 * pageSize)

    override fun getPagingPhotos(orderBy: String): Flow<PagingData<Unsplash>> {
        @OptIn(ExperimentalPagingApi::class)
        val pager = Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                maxSize = pageSize + 2 * prefetchDist,
                prefetchDistance = prefetchDist,
                initialLoadSize = initialLoadSize
            ),
            remoteMediator = UnsplashRemoteMediator(unsplashDatabase, apiService, orderBy),
            pagingSourceFactory = {
                unsplashDatabase.unsplashDao().getPagingPhotos()
            }
        )
        return pager.flow.map { data -> data.map { it.toDomain() } }
    }

    override fun getFavoriteList(): Flow<PagingData<Unsplash>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                maxSize = pageSize + 2 * prefetchDist,
                prefetchDistance = prefetchDist,
                initialLoadSize = initialLoadSize
            ),
            pagingSourceFactory = {
                UnsplashPagingSource(unsplashDatabase)
            }
        )
        return pager.flow.map { data -> data.map { it.toDomain() } }
    }

    override fun searchPhotos(query: String): Flow<PagingData<Unsplash>> {
        @OptIn(ExperimentalPagingApi::class)
        val pager = Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                maxSize = pageSize + 2 * prefetchDist,
                prefetchDistance = prefetchDist,
                initialLoadSize = initialLoadSize
            ),
            remoteMediator = SearchRemoteMediator(unsplashDatabase, apiService, query),
            pagingSourceFactory = {
                unsplashDatabase.unsplashDao().getPagingPhotos()
            }
        )
        return pager.flow.map { data -> data.map { it.toDomain() } }
    }

    override suspend fun getDetail(id: String): Response<UnsplashDetail> {
        val detail = DetailApiServiceImpl(apiService).getDetail(id)
        return detail.toResponseDetail()
    }

    override fun setFavorite(photo: Unsplash, isFavorite: Boolean){
        val unsplashEntity = photo.toEntity()
        unsplashEntity.isFavorite = isFavorite
        unsplashDatabase.unsplashDao().updateFavoriteImage(unsplashEntity)
        val favoriteEntity = photo.toFavoriteEntity()
        if (isFavorite) unsplashDatabase.unsplashFavoriteDao().addFavorite(favoriteEntity)
        else unsplashDatabase.unsplashFavoriteDao().deleteFavorite(photo.id)
    }

}