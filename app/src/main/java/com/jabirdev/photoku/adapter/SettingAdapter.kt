package com.jabirdev.photoku.adapter

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jabirdev.photoku.R
import com.jabirdev.photoku.app.AppConstants
import com.jabirdev.photoku.databinding.ItemLogoBinding
import com.jabirdev.photoku.databinding.ItemMenuBinding
import com.jabirdev.photoku.model.MenuSetting
import com.jabirdev.photoku.util.getMyTheme
import com.jabirdev.photoku.util.setMyTheme
import java.util.*

class SettingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var oldList = mutableListOf<MenuSetting>()

    class LogoHolder(private val binding: ItemLogoBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(data: MenuSetting){
            binding.ivAboutPhoto.setImageResource(data.icon)
            binding.tvTitle.text = data.title
        }
    }

    class MenuHolder(private val binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root){
        private val context = binding.root.context
        private val list: Array<String> = context.resources.getStringArray(R.array.menu_title)
        private val myThemes = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            context.resources.getStringArray(R.array.my_themes_2)
        } else {
            context.resources.getStringArray(R.array.my_themes_1)
        }
        private val myLang get() = Locale.getDefault().displayName

        fun setData(data: MenuSetting){
            binding.ivItemAboutIcon.setImageResource(data.icon)
            binding.tvItemAboutTitle.text = data.title

            if (data.title == list[0]) binding.tvItemAboutDescription.text = myLang

            if (data.title == list[1]) binding.tvItemAboutDescription.text = myThemes[getMyTheme(context)]

            itemView.setOnClickListener {
                when(data.title){
                    list[0] -> {
                        context.startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    }
                    list[1] -> {
                        val builder = MaterialAlertDialogBuilder(context)
                        builder.setTitle(data.title)
                        builder.setSingleChoiceItems(myThemes, getMyTheme(context)) { d, position ->
                            when (position) {
                                getMyTheme(context) -> d.dismiss()
                                0 -> {
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                        setMyTheme(context, MODE_NIGHT_AUTO_BATTERY)
                                        setDefaultNightMode(MODE_NIGHT_AUTO_BATTERY)
                                    } else {
                                        setMyTheme(context, MODE_NIGHT_FOLLOW_SYSTEM)
                                        setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                                    }
                                }
                                1 -> {
                                    setMyTheme(context, MODE_NIGHT_NO)
                                    setDefaultNightMode(MODE_NIGHT_NO)
                                }
                                2 -> {
                                    setMyTheme(context, MODE_NIGHT_YES)
                                    setDefaultNightMode(MODE_NIGHT_YES)
                                }
                            }
                            d.dismiss()
                            if (data.title == list[1]) binding.tvItemAboutDescription.text = myThemes[getMyTheme(context)]
                            Toast.makeText(context, myThemes[position], Toast.LENGTH_SHORT).show()
                        }
                        builder.show()
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return oldList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            AppConstants.MENU_ABOUT_TYPE_ACCOUNT -> {
                val view = ItemLogoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LogoHolder(view)
            }
            else -> {
                val view = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MenuHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            AppConstants.MENU_ABOUT_TYPE_ACCOUNT -> (holder as LogoHolder).setData(oldList[position])
            else -> (holder as MenuHolder).setData(oldList[position])
        }
    }

    fun setData(newList: MutableList<MenuSetting>){
        val diffUtil = MenuDiffUtil(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class MenuDiffUtil(
        private val oldList: List<MenuSetting>,
        private val newList: List<MenuSetting>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].type == newList[newItemPosition].type &&
                    oldList[oldItemPosition].icon == newList[newItemPosition].icon &&
                    oldList[oldItemPosition].title == newList[newItemPosition].title
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}