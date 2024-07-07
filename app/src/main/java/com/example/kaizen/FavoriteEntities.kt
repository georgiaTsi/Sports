package com.example.kaizen

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity//TODO
data class FavoriteSport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

@Entity
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)