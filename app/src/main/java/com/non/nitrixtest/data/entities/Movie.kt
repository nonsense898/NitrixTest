package com.non.nitrixtest.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey() @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "sources")
    val sources: List<String>,

    @ColumnInfo(name = "thumb")
    val thumb: String,

    @ColumnInfo(name = "subtitle")
    val subtitle: String
)

