package com.example.kaizen.viewmodel

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kaizen.DatabaseHelper
import com.example.kaizen.R
import com.example.kaizen.SportAdapter
import com.example.kaizen.api.RetrofitInstance
import com.example.kaizen.api.SportsApi
import com.example.kaizen.model.Sport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val sportsApi: SportsApi, private val dbHelper: DatabaseHelper) : ViewModel() {
    private val _sports = MutableLiveData<Resource<List<Sport>>>()
    val sports: LiveData<Resource<List<Sport>>> = _sports

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchSports()
    }

    fun fetchSports() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                sportsApi.getSportsEvents().enqueue(object : Callback<List<Sport>> {
                    override fun onResponse(call: Call<List<Sport>>, response: Response<List<Sport>>) {
                        if (response.isSuccessful) {
                            val fetchedSports = response.body() ?: emptyList()
                            val favoriteSports = dbHelper.getAllFavoriteSports()
                            val favoriteEvents = dbHelper.getAllFavoriteEvents()

                            fetchedSports.forEach { sport ->
                                sport.isStarred = favoriteSports.contains(sport.name)
                                sport.events.forEach { event ->
                                    event.isFavorite = favoriteEvents.contains(event.eventId)
                                }
                                sport.isExpanded = true
                            }

                            _sports.value = Resource.Success(fetchedSports)
                        } else {
                            _sports.value = Resource.Error("API Error")
                        }
                        _isLoading.value = false // Hide loading in onResponse
                    }

                    override fun onFailure(call: Call<List<Sport>>, t: Throwable) {
                        _sports.value = Resource.Error("Error: ${t.message}")
                        _isLoading.value = false // Hide loading in onFailure
                    }
                })
            } catch (e: Exception) {
                _sports.value = Resource.Error("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Resource class to handle loading, success, and error states
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}