package com.ragvax.picttr.domain.photo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ragvax.picttr.data.photo.PhotoService
import com.ragvax.picttr.data.photo.model.Photo
import com.ragvax.picttr.domain.photo.PhotoPagingSource
import com.ragvax.picttr.domain.photo.TopicPhotoPagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(private val photoService: PhotoService) {

    fun getPhotos(): Flow<PagingData<Photo>> =
        Pager(
            config = PagingConfig(
                initialLoadSize = 15,
                pageSize = 15,
                maxSize = 100,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PhotoPagingSource(photoService) }
        ).flow

    fun getTopicPhotos(topicId: String): Flow<PagingData<Photo>> =
        Pager(
            config = PagingConfig(
                initialLoadSize = 15,
                pageSize = 15,
                maxSize = 100,
                prefetchDistance = 5,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { TopicPhotoPagingSource(photoService, topicId) }
        ).flow

    suspend fun getPhotoDetails(id: String) = photoService.getPhoto(id)
}