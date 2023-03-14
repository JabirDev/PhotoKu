package com.jabirdev.core.api

import com.jabirdev.core.data.source.remote.response.UnsplashItem
import retrofit2.Response

interface ApiHelper {

    suspend fun getDetail(id: String): Response<UnsplashItem>

}