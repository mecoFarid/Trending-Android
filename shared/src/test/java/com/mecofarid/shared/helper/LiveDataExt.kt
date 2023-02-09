package com.mecofarid.shared.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withTimeout

suspend fun <T> LiveData<T>.awaitValues(time: Long = 5_000): List<T> {
    val data = mutableListOf<T>()
    try {
        withTimeout(time) {
            callbackFlow<Unit> {
                val observer = Observer<T> { o ->
                    data.add(o)
                }
                observeForever(observer)
                awaitClose {
                    removeObserver(observer)
                }
            }.collect()
        }
    } catch (ignored: TimeoutCancellationException){}

    return data
}

