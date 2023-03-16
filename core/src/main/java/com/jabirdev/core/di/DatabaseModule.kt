package com.jabirdev.core.di

import android.content.Context
import androidx.room.Room
import com.jabirdev.core.data.source.local.room.UnsplashDao
import com.jabirdev.core.data.source.local.room.UnsplashDatabase
import com.jabirdev.core.data.source.local.room.UnsplashFavoriteDao
import com.jabirdev.core.data.source.local.room.UnsplashRemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : UnsplashDatabase {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("Unsplash".toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            context,
            UnsplashDatabase::class.java, "Unsplash.db")
            .fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    fun provideUnsplashDao(database: UnsplashDatabase): UnsplashDao = database.unsplashDao()

    @Provides
    fun provideUnsplashFavoriteDao(database: UnsplashDatabase): UnsplashFavoriteDao = database.unsplashFavoriteDao()

    @Provides
    fun provideUnsplashKeysDao(database: UnsplashDatabase): UnsplashRemoteKeysDao = database.unsplashRemoteKeysDao()

}