package com.ragvax.picttr.domain.topic

import com.ragvax.picttr.data.topic.TopicService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopicRepository @Inject constructor(private val topicService: TopicService) {

    suspend fun getTopics(per_page: Int = 15) = topicService.getTopics(per_page)
}