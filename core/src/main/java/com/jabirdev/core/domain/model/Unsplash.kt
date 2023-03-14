package com.jabirdev.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Unsplash (
    val id: String,
    var isFavorite: Boolean,
    var createdAt: String,
    var updatedAt: String,
    var rawImage: String,
    var fullImage: String,
    var regularImage: String,
    var smallImage: String,
    var thumbImage: String,
    var smallS3Image: String,
    var selfLink: String,
    var htmlLink: String
) : Parcelable