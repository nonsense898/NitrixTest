package com.non.nitrixtest.data.entities

data class Category(
    val name: String,
    val videos: List<Movie>
) {
    fun withHttps(): Category {
        return copy(videos = videos.map { it.withHttps() })
    }
}
