package com.ragvax.picttr.domain.photo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ragvax.picttr.data.photo.PhotoService
import com.ragvax.picttr.data.photo.model.Photo
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class TopicPhotoPagingSource(
    private val service: PhotoService,
    private val topicId: String
) : PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val photos = service.getTopicPhotos(topicId, page, params.loadSize)
            LoadResult.Page(
                data = photos,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (photos.isEmpty()) null else page + 1,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition
    }
}