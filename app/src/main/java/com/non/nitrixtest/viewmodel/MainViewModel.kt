package com.non.nitrixtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.non.nitrixtest.data.entities.Movie
import com.non.nitrixtest.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _movieData = MutableLiveData<List<Movie>>()
    val movieData: LiveData<List<Movie>> = _movieData

    fun setCurrentIndex(index: Int) {
        _currentIndex.value = index
    }

    val movies: LiveData<List<Movie>> = repository.getLocalMovies()

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    fun fetchMovies() {
        viewModelScope.launch {
            try {
                val movies = withContext(Dispatchers.IO) {
                    repository.fetchMovies()
                }
                _movieData.postValue(movies)
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching movies: ${e.message}")
            }
        }
    }

    fun insertMovies(movies: List<Movie>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertMovies(movies)
        }
    }


}