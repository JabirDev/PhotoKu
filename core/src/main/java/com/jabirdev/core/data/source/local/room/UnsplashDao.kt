package com.jabirdev.core.data.source.local.room

import androidx.paging.PagingSource
import androidx.room.*
import com.jabirdev.core.data.source.local.entity.UnsplashEntity

@Dao
interface UnsplashDao {

    @Query("SELECT * FROM unsplash")
    fun getPagingPhotos(): PagingSource<Int, UnsplashEntity>

    @Query("SELECT * FROM unsplash")
    fun getAllPhotos(): List<UnsplashEntity>

    @Query("SELECT * FROM unsplash WHERE id = :id")
    fun getDetailPhoto(id: String) : UnsplashEntity

    @Query("SELECT * FROM unsplash WHERE is_favorite = 1 ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getFavoriteImage(limit: Int, offset: Int): List<UnsplashEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(image: List<UnsplashEntity>)

    @Update
    fun updateFavoriteImage(photo: UnsplashEntity)

    @Query("DELETE from unsplash")
    suspend fun deleteAll()

    @Query("DELETE from unsplash WHERE id = :id")
    suspend fun deletePhoto(id: String)

}