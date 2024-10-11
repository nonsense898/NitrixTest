package com.non.nitrixtest.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.non.nitrixtest.data.entities.Movie
import com.non.nitrixtest.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _fetchedMovies = MutableLiveData<List<Movie>>()
    private val fetchedMovies: LiveData<List<Movie>> = _fetchedMovies

    private val localMovies: LiveData<List<Movie>> = repository.getLocalMovies()

    private var isFetching = false

    var exoPlayer: ExoPlayer? = null

    fun initExoPlayer(context: Context) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
        exoPlayer = null
    }

    val movieData: LiveData<List<Movie>> = MediatorLiveData<List<Movie>>().apply {
        addSource(localMovies) { localData ->
            if (localData.isNullOrEmpty()) {
                fetchMovies()
            } else {
                value = localData
            }
        }
        addSource(fetchedMovies) { fetchedData ->
            if (localMovies.value.isNullOrEmpty()) {
                value = fetchedData
            }
        }
    }

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    fun setCurrentIndex(index: Int) {
        _currentIndex.value = index
    }

    private fun fetchMovies() {
        if (isFetching) return
        isFetching = true
        viewModelScope.launch {
            try {
                val moviesFromApi = repository.fetchMovies()
                _fetchedMovies.value = moviesFromApi
                insertMovies(moviesFromApi)
            } catch (e: Exception) {
                _errorMessage.value = "Unable to fetch movies: ${e.message}"
            } finally {
                isFetching = false
            }
        }
    }

    private fun insertMovies(movies: List<Movie>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertMovies(movies)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Failed to save movies: ${e.message}"
                }
            }
        }
    }
}