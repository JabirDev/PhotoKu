package com.jabirdev.core.data.source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jabirdev.core.api.ApiService
import com.jabirdev.core.data.source.local.entity.UnsplashEntity
import com.jabirdev.core.data.source.local.entity.UnsplashRemoteKeys
import com.jabirdev.core.data.source.local.room.UnsplashDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class SearchRemoteMediator @Inject constructor(
    private val database: UnsplashDatabase,
    private val apiService: ApiService,
    private val query: String
) : RemoteMediator<Int, UnsplashEntity>(){

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            val responseData = apiService.searchPhotos(query = query, page = page, perPage = state.config.pageSize)
            val photosList = responseData.results
            val endOfPaginationReached = photosList.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.unsplashRemoteKeysDao().deleteRemoteKeys()
                    database.unsplashDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = photosList.map {
                    UnsplashRemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.unsplashRemoteKeysDao().insertAll(keys)
                val unsplashEntityList = ArrayList<UnsplashEntity>()
                photosList.forEach {
                    val favoriteId = database.unsplashFavoriteDao().getFavorite(it.id)
                    val isFavorite = favoriteId?.id == it.id
                    val unsplash = UnsplashEntity(
                        id = it.id, isFavorite = isFavorite, createdAt = it.createdAt, updatedAt = it.updatedAt, 
                        rawImage = it.urls.raw, fullImage = it.urls.full, regularImage = it.urls.regular, smallImage = it.urls.small, thumbImage = it.urls.thumb, smallS3Image = it.urls.smallS3,
                        selfLink = it.links.self, htmlLink = it.links.html
                    )
                    unsplashEntityList.add(unsplash)
                }
                database.unsplashDao().insertImage(unsplashEntityList)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception){
            MediatorResult.Error(e)
        }

    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UnsplashEntity>): UnsplashRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.unsplashRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UnsplashEntity>): UnsplashRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.unsplashRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UnsplashEntity>): UnsplashRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.unsplashRemoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}