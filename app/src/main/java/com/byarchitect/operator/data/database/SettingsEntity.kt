package com.byarchitect.operator.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val key: String,
    val value: String
)
