package com.jabirdev.core.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.jabirdev.core.data.source.local.entity.UnsplashFavorite
import com.jabirdev.core.data.source.local.room.UnsplashDatabase
import kotlinx.coroutines.delay

class UnsplashPagingSource(
    private val unsplashDatabase: UnsplashDatabase
) : PagingSource<Int, UnsplashFavorite>(){
    override fun getRefreshKey(state: PagingState<Int, UnsplashFavorite>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashFavorite> {
        val page = params.key ?: 0

        return try {
            unsplashDatabase.withTransaction {
//                val entities = unsplashDatabase.unsplashDao().getAllPhotos()
//                entities.forEach {
//                    val favorite = unsplashDatabase.unsplashFavoriteDao().getFavorite(it.id)
//                    if (favorite != null && favorite.id == it.id){
//                        it.isFavorite = true
//                        unsplashDatabase.unsplashDao().updateFavoriteImage(it)
//                    }
//                }
                val favoriteList = unsplashDatabase.unsplashFavoriteDao().getFavoriteList(params.loadSize, page * params.loadSize)
                if (page != 0) delay(1000)
                LoadResult.Page(
                    data = favoriteList,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (favoriteList.isEmpty()) null else page + 1
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}

