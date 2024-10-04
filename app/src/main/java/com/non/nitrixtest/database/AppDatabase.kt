package com.non.nitrixtest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.non.nitrixtest.dao.MovieDao
import com.non.nitrixtest.data.entities.Movie
import com.non.nitrixtest.utils.Converters

@TypeConverters(Converters::class)
@Database(entities = [Movie::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}