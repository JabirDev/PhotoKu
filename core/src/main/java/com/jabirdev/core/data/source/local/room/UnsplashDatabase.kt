package com.jabirdev.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jabirdev.core.data.source.local.entity.UnsplashEntity
import com.jabirdev.core.data.source.local.entity.UnsplashFavorite
import com.jabirdev.core.data.source.local.entity.UnsplashRemoteKeys

@Database(
    entities = [UnsplashEntity::class, UnsplashRemoteKeys::class, UnsplashFavorite::class],
    version = 1,
    exportSchema = false
)
abstract class UnsplashDatabase : RoomDatabase() {

    abstract fun unsplashDao(): UnsplashDao
    abstract fun unsplashFavoriteDao(): UnsplashFavoriteDao
    abstract fun unsplashRemoteKeysDao(): UnsplashRemoteKeysDao

//    companion object {
//        @Volatile
//        private var INSTANCE: UnsplashDatabase? = null
//
//        @JvmStatic
//        fun getInstance(context: Context): UnsplashDatabase {
//            return INSTANCE ?: synchronized(this) {
//                INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    UnsplashDatabase::class.java,
//                    "Unsplash.db"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                    .also {
//                        INSTANCE = it
//                    }
//            }
//        }
//    }

}