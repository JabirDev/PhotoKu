package com.jabirdev.core.data.source.remote

import com.jabirdev.core.api.ApiHelper
import com.jabirdev.core.api.ApiService
import com.jabirdev.core.data.source.remote.response.UnsplashItem
import retrofit2.Response
import javax.inject.Inject

class DetailApiServiceImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getDetail(id: String): Response<UnsplashItem> = apiService.getDetail(id)

}