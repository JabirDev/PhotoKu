package com.jabirdev.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jabirdev.core.data.source.local.entity.UnsplashFavorite

@Dao
interface UnsplashFavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(favorite: UnsplashFavorite)

    @Query("SELECT * from favorite WHERE id = :id")
    fun getFavorite(id: String) : UnsplashFavorite?

    @Query("SELECT * from favorite ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getFavoriteList(limit: Int, offset: Int) : List<UnsplashFavorite>

    @Query("DELETE from favorite WHERE id = :id")
    fun deleteFavorite(id: String)

    @Query("DELETE from favorite")
    fun deleteAll()

}