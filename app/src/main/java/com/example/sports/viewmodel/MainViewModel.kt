package com.example.sports.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sports.data.DatabaseHelper
import com.example.sports.api.SportsApi
import com.example.sports.data.Event
import com.example.sports.data.Sport
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface MainViewModelContract {
    fun toggleSportExpansion(sport: Sport)
    fun toggleFavoriteSport(sport: Sport)
}

open class MainViewModel(private val sportsApi: SportsApi, private val dbHelper: DatabaseHelper) : ViewModel(), MainViewModelContract {
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

    override fun toggleSportExpansion(sport: Sport) {
        sport.isExpanded = !sport.isExpanded
    }

    override fun toggleFavoriteSport(sport: Sport) {
        sport.isStarred = !sport.isStarred
        if (sport.isStarred) {
            dbHelper.addFavoriteSport(sport.name)
        } else {
            dbHelper.removeFavoriteSport(sport.name)
        }
    }

    fun toggleFavoriteEvent(event: Event) {
        event.isFavorite = !event.isFavorite
        if (event.isFavorite) {
            dbHelper.addFavoriteEvent(event.eventId)
        } else {
            dbHelper.removeFavoriteEvent(event.eventId)
        }
    }
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}