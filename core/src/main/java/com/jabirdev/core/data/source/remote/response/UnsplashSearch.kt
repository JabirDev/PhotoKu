package com.jabirdev.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UnsplashSearch(
    @SerializedName("total") val total: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<UnsplashItem>

)
