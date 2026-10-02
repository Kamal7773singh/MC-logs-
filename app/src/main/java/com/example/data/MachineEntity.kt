package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "machines")
data class MachineEntity(
    @PrimaryKey val id: String,
    val name: String,
    val brand: String,
    val totalTime: Long, // in seconds
    val isRunning: Boolean,
    val startAt: Long?
)
