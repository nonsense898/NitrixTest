package com.non.nitrixtest.repository

import androidx.lifecycle.LiveData
import com.non.nitrixtest.dao.ApiService
import com.non.nitrixtest.dao.MovieDao
import com.non.nitrixtest.data.entities.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieRepository @Inject constructor(
    private val apiService: ApiService,
    private val movieDao: MovieDao
) {

    suspend fun fetchMovies(): List<Movie> {
        return withContext(Dispatchers.IO) {
            apiService.getMediaData().withHttps().categories.flatMap { category ->
                category.videos.map { video ->
                    Movie(
                        title = video.title,
                        description = video.description,
                        sources = video.sources,
                        subtitle = video.subtitle,
                        thumb = video.thumb
                    )
                }
            }
        }
    }

    suspend fun insertMovies(movies: List<Movie>) = withContext(Dispatchers.IO) {
        movieDao.insertMovies(movies)
    }

    fun getLocalMovies(): LiveData<List<Movie>> = movieDao.getAllMovies()
}

