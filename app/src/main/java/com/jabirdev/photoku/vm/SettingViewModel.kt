package com.jabirdev.photoku.vm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jabirdev.photoku.R
import com.jabirdev.photoku.app.AppConstants
import com.jabirdev.photoku.model.MenuSetting

class SettingViewModel : ViewModel() {

    private val _menuList = MutableLiveData<MutableList<MenuSetting>>()
    val menuList: LiveData<MutableList<MenuSetting>> get() = _menuList

    fun getMenuList(context: Context){
        val titleList = context.resources.getStringArray(R.array.menu_title)
        val iconList = arrayListOf(
            R.drawable.ic_round_translate_24,
            R.drawable.ic_round_dark_mode
        )
        val mMenuList = ArrayList<MenuSetting>()
        val appLogo = MenuSetting(
            AppConstants.MENU_ABOUT_TYPE_ACCOUNT, context.getString(R.string.app_name), R.drawable.ic_launcher_background
        )
        mMenuList.add(appLogo)
        titleList.forEachIndexed { index, s ->
            val menu = MenuSetting(
                AppConstants.MENU_ABOUT_TYPE_ITEM,
                s,
                iconList[index]
            )
            mMenuList.add(menu)
        }
        _menuList.value = mMenuList
    }

}