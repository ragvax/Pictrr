package com.ragvax.picttr.domain.topic

import com.ragvax.picttr.data.topic.TopicService
import com.ragvax.picttr.data.topic.model.Topic
import com.ragvax.picttr.utils.NetworkConnectivity
import com.ragvax.picttr.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopicRepository @Inject constructor(
    private val topicService: TopicService,
    private val networkConnectivity: NetworkConnectivity,) {

    suspend fun getTopics(per_page: Int = 15): Resource<List<Topic>> {
        if (!networkConnectivity.isConnected()) {
            return Resource.Error("No internet connection")
        }

        return try {
            val response = topicService.getTopics(per_page)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch(e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}