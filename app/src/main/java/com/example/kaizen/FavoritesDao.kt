package com.example.kaizen

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao//TODO
interface FavoriteSportDao {
    @Insert
    suspend fun insert(favoriteSport: FavoriteSport)

    @Delete
    suspend fun delete(favoriteSport: FavoriteSport)

    @Query("SELECT * FROM FavoriteSport")
    fun getAll(): Flow<List<FavoriteSport>>
}

@Dao
interface FavoriteEventDao {
    @Insert
    suspend fun insert(favoriteEvent: FavoriteEvent)

    @Delete
    suspend fun delete(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM FavoriteEvent")
    fun getAll(): Flow<List<FavoriteEvent>>
}