package com.jabirdev.photoku.model

import com.google.gson.annotations.SerializedName

data class MenuSetting(
    @SerializedName("type") val type: Int,
    @SerializedName("title") val title: String,
    @SerializedName("icon") val icon: Int
)