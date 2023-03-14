package com.jabirdev.photoku.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.jabirdev.photoku.util.getMyTheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (getMyTheme(this) != 0) AppCompatDelegate.setDefaultNightMode(getMyTheme(this))
    }

}