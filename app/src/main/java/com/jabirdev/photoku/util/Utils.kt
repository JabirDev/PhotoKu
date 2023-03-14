package com.jabirdev.photoku.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate.*
import com.jabirdev.photoku.app.AppConstants.PREF_APP
import com.jabirdev.photoku.app.AppConstants.PREF_THEME

fun getMyTheme(context: Context) : Int {
    val sPref = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
    return when (sPref.getInt(PREF_THEME, 0)) {
        MODE_NIGHT_FOLLOW_SYSTEM, MODE_NIGHT_AUTO_BATTERY -> 0
        MODE_NIGHT_NO -> 1
        MODE_NIGHT_YES -> 2
        else -> 0
    }
}

fun setMyTheme(context: Context, mode: Int) {
    val sPref = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
    val editor = sPref.edit()
    editor.putInt(PREF_THEME, mode)
    editor.apply()
}
