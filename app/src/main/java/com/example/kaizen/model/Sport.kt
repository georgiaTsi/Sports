package com.example.kaizen.model

import com.google.gson.annotations.SerializedName

data class Sport(
    @SerializedName("i")
    val code: String,

    @SerializedName("d")
    val name: String,

    @SerializedName("e")
    val events: List<Event>,

    var isStarred: Boolean = false
)