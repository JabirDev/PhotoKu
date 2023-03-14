package com.jabirdev.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jabirdev.core.data.source.local.entity.UnsplashRemoteKeys

@Dao
interface UnsplashRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<UnsplashRemoteKeys>)

    @Query("SELECT * from remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): UnsplashRemoteKeys?

    @Query("DELETE from remote_keys")
    suspend fun deleteRemoteKeys()

}