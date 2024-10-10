package com.non.nitrixtest.network.response

import com.non.nitrixtest.data.entities.Category

data class MediaResponse(
    val categories: List<Category>
) {
    fun withHttps(): MediaResponse {
        return copy(categories = categories.map { it.withHttps() })
    }
}