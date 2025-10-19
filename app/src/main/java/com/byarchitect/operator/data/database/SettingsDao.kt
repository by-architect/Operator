package com.byarchitect.operator.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SettingsDao {

    @Query("SELECT value FROM settings WHERE key = :key")
    suspend fun getValue(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setValue(setting: SettingsEntity)

    @Query("DELETE FROM settings WHERE key = :key")
    suspend fun deleteValue(key: String)
}
