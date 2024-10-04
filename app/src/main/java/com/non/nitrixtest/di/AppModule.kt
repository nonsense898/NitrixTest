package com.non.nitrixtest.di

import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.non.nitrixtest.dao.MovieDao
import com.non.nitrixtest.database.AppDatabase
import com.non.nitrixtest.repository.MovieRepository
import com.non.nitrixtest.utils.Converters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "movie_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: AppDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(gson: Gson, movieDao: MovieDao): MovieRepository {
        return MovieRepository(gson, movieDao)
    }
}