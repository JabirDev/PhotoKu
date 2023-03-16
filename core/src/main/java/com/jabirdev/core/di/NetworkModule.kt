package com.jabirdev.core.di

import com.jabirdev.core.BuildConfig
import com.jabirdev.core.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val baseUrl = "https://api.unsplash.com/"

    @Provides
    fun provideLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val hostname = baseUrl.replace("https://", "").replace("/", "")
        val certification = CertificatePinner.Builder()
            .add(hostname, "sha256/dUyxlTICtMn4wEys0D5M3xvwgNyHj0lK4lT7tdqD/O0=")
            .add(hostname, "sha256/0OyeXCoPbY19oU2881iW7i1bAu8Ni+HMKGN08r/G5XI=")
            .build()
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val url = chain
                    .request()
                    .url
                    .newBuilder()
                    .addQueryParameter("client_id", BuildConfig.UNSPLASH_CLIENT_ID)
                    .build()
                chain.proceed(chain.request().newBuilder().url(url).build())
            }
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certification)
            .build()
    }

    @Provides
    fun provideApiService(client: OkHttpClient) : ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

}