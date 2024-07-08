package com.example.kaizen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kaizen.DatabaseHelper
import com.example.kaizen.api.SportsApi

class MainViewModelFactory(private val sportsApi: SportsApi, private val dbHelper: DatabaseHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(sportsApi, dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}