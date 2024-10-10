package com.non.nitrixtest.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.non.nitrixtest.data.entities.Movie
import com.non.nitrixtest.network.response.MediaResponse
import retrofit2.Call
import retrofit2.http.GET

@Dao
interface MovieDao {
    @Query("DELETE FROM movies")
    suspend fun deleteAll()

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE title = :title LIMIT 1")
    fun getMovieByTitle(title: String): LiveData<Movie?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("DELETE FROM movies WHERE title = :title")
    suspend fun deleteMovie(title: String)
}