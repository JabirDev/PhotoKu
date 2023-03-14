package com.jabirdev.core.api

import com.jabirdev.core.data.source.remote.response.UnsplashItem
import com.jabirdev.core.data.source.remote.response.UnsplashSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/photos")
    suspend fun getPhotos(
        @Query("order_by") orderBy: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null
    ) : List<UnsplashItem>

    @GET("/photos/{id}")
    suspend fun getDetail(
        @Path("id") id: String
    ) : Response<UnsplashItem>

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null
    ) : UnsplashSearch

}