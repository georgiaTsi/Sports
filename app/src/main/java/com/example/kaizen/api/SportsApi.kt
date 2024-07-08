package com.example.kaizen.api

import com.example.kaizen.data.Sport
import retrofit2.Call
import retrofit2.http.GET

interface SportsApi {
    @GET("sports")
    fun getSportsEvents(): Call<List<Sport>>
}