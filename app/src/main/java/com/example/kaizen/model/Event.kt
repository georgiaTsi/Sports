package com.example.kaizen.model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("i")
    val eventId: String,

    @SerializedName("d")
    val eventDescription: String,

    @SerializedName("sh")
    val shortDescription: String,

    @SerializedName("si")
    val sportCode: String,

    @SerializedName("tt")
    val timestamp: Long,

    var isFavorite: Boolean = false
)