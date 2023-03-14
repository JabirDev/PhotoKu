package com.jabirdev.core.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "unsplash")
data class UnsplashEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean,

    @ColumnInfo(name = "created_at")
    var createdAt: String,

    @ColumnInfo(name = "updated_at")
    var updatedAt: String,

    @ColumnInfo(name = "raw")
    var rawImage: String,

    @ColumnInfo(name = "full")
    var fullImage: String,

    @ColumnInfo(name = "regular")
    var regularImage: String,

    @ColumnInfo(name = "small")
    var smallImage: String,

    @ColumnInfo(name = "thumb")
    var thumbImage: String,

    @ColumnInfo(name = "small_s3")
    var smallS3Image: String,

    @ColumnInfo(name = "self")
    var selfLink: String,

    @ColumnInfo(name = "html")
    var htmlLink: String

) : Parcelable
