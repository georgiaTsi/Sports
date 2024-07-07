package com.example.kaizen

import com.example.kaizen.model.Sport
import retrofit2.Call
import retrofit2.http.GET

interface SportsApi {
    @GET("sports")
    fun getSportsEvents(): Call<List<Sport>>
}