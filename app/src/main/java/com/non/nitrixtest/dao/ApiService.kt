package com.non.nitrixtest.dao

import com.non.nitrixtest.network.response.MediaResponse
import retrofit2.http.GET

interface ApiService {
    @GET("3b19447b304616f18657/raw/gistfile1.txt")
    suspend fun getMediaData(): MediaResponse
}