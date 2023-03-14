package com.jabirdev.core.data.source.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class UnsplashRemoteKeys (
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)