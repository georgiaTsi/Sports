package com.example.sports.api

import com.example.sports.data.Sport
import retrofit2.Call
import retrofit2.http.GET

interface SportsApi {
    @GET("sports")
    fun getSportsEvents(): Call<List<Sport>>
}