package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey val id: String,
    val machineId: String,
    val machineName: String,
    val action: String,
    val duration: Long,
    val time: Long
)
