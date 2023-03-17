package com.jabirdev.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun <T : Any, V : RecyclerView.ViewHolder> PagingDataAdapter<T, V>.withLoadStateAdapters(
    header: LoadStateAdapter<*>,
    footer: LoadStateAdapter<*>
): ConcatAdapter {
    addLoadStateListener { loadStates ->
        header.loadState = loadStates.refresh
        footer.loadState = loadStates.append
    }

    return ConcatAdapter(header, this, footer)
}

fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    return actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
}
fun countViews(count:Long): String{
    val array = arrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
    val value = floor(log10(count.toDouble())).toInt()
    val  base = value / 3
    if (value >= 3 && base < array.size) {
        return DecimalFormat("#0.0").format(count/ 10.0.pow((base * 3).toDouble())) + array[base]
    } else {
        return DecimalFormat("#,##0").format(count)
    }
}

