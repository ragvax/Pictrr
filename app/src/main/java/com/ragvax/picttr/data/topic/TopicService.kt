package com.ragvax.picttr.data.topic

import com.ragvax.picttr.BuildConfig
import com.ragvax.picttr.data.topic.model.Topic
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TopicService {

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("topics")
    suspend fun getTopics(@Query("per_page") per_page: Int): Response<List<Topic>>

    companion object {
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
        const val BASE_URL = "https://api.unsplash.com/"
    }
}