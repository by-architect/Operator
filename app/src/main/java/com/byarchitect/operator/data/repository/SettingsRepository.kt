package com.byarchitect.operator.data.repository

import android.content.Context
import com.byarchitect.operator.data.database.AppDatabase
import com.byarchitect.operator.data.database.SettingsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsRepository(context: Context) {

    private val settingsDao = AppDatabase.getDatabase(context).settingsDao()

    companion object {
        private const val KEY_REFRESH_RATE = "refresh_rate"
        private const val DEFAULT_REFRESH_RATE = 3000L
    }

    suspend fun getRefreshRate(): Long {
        return withContext(Dispatchers.IO) {
            val value = settingsDao.getValue(KEY_REFRESH_RATE)
            value?.toLongOrNull() ?: DEFAULT_REFRESH_RATE
        }
    }

    suspend fun setRefreshRate(rate: Long) {
        withContext(Dispatchers.IO) {
            settingsDao.setValue(
                SettingsEntity(
                    key = KEY_REFRESH_RATE,
                    value = rate.toString()
                )
            )
        }
    }
}