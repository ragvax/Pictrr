package com.ragvax.picttr.data.photo

import com.ragvax.picttr.BuildConfig
import com.ragvax.picttr.data.photo.model.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoService {

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<Photo>

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("photos/{id}")
    suspend fun getPhoto(@Path("id") id: String): Response<Photo>

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("topics/{id}/photos")
    suspend fun getTopicPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<Photo>

    companion object {
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }
}