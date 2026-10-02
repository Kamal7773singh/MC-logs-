package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.SettingsRepository

class FieldCommandApplication : Application() {
    val database by lazy { Room.databaseBuilder(this, AppDatabase::class.java, "field_command_db").build() }
    val repository by lazy { AppRepository(database.appDao()) }
    val settingsRepository by lazy { SettingsRepository(this) }
}
