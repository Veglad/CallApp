package com.veglad.callapp.helpers

import kotlinx.coroutines.*

fun startCoroutineTimer(delayMillis: Long = 0, repeatMillis: Long = 0, action: () -> Unit) = GlobalScope.launch {
    delay(delayMillis)
    if (repeatMillis > 0) {
        while (true) {
            withContext(Dispatchers.Main) { action() }
            delay(repeatMillis)
        }
    } else {
        action()
    }
}