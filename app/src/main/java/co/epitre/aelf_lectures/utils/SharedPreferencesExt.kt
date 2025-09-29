package co.epitre.aelf_lectures.utils

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun SharedPreferences.stringFlow(key: String, default: String): Flow<String> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
        if (changedKey == key) trySend(getString(key, default) ?: default)
    }
    registerOnSharedPreferenceChangeListener(listener)
    trySend(getString(key, default) ?: default)
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}