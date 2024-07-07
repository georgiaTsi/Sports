package com.example.kaizen

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    lateinit var appDatabase:AppDatabase

    fun initialize(context: Context) {
        appDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "favorites_database"
        ).build()
    }
}