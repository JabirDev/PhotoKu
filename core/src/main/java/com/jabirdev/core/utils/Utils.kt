package com.jabirdev.core.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

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

@FlowPreview
fun <T, R> Flow<T>.switchMap(
    mapper: suspend (value: T) -> Flow<R>
): Flow<R> {
    return flow {
        coroutineScope {
            var currentJob: Job? = null
            collect { outerValue ->
                val inner = mapper(outerValue)
                currentJob?.cancelAndJoin()
                currentJob = launch {
                    inner.collect { value ->
                        emit(value)
                    }
                }
            }
        }
    }
}
