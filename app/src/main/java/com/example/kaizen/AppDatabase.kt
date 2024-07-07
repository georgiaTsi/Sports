package com.example.kaizen

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteSport::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteSportDao(): FavoriteSportDao

    abstract fun favoriteEventDao(): FavoriteEventDao
}