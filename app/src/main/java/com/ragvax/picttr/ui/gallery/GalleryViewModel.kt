package com.ragvax.picttr.ui.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ragvax.picttr.data.photo.model.Photo
import com.ragvax.picttr.data.topic.model.Topic
import com.ragvax.picttr.domain.photo.PhotoRepository
import com.ragvax.picttr.domain.topic.TopicRepository
import com.ragvax.picttr.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    private val photoRepository: PhotoRepository,
    private val state: SavedStateHandle,
) : ViewModel() {

    init {
        getTopics()
    }

//    val photos = repository.getPhotos().cachedIn(viewModelScope)
    private val _topicsFlow = MutableStateFlow<TopicsEvent>(TopicsEvent.Empty)
    val topicsFlow: StateFlow<TopicsEvent> = _topicsFlow

    private val photosChannel = Channel<String?>(Channel.CONFLATED)

    private val galleryEventsChannel = Channel<GalleryEvent>(Channel.CONFLATED)
    val galleryEvent = galleryEventsChannel.receiveAsFlow()

    @ExperimentalCoroutinesApi
    @FlowPreview
    val photosFlow = flowOf(
        photosChannel.receiveAsFlow().map { PagingData.empty() },
        state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)
            .asFlow()
            .flatMapLatest { id ->
                photoRepository.getTopicPhotos(id)
            }
            .cachedIn(viewModelScope)
    ).flattenMerge()

    fun getTopics() = viewModelScope.launch(Dispatchers.IO) {
        _topicsFlow.value = TopicsEvent.Loading
        when(val result = topicRepository.getTopics()) {
            is Resource.Success -> {
                val data = result.data
                if (data != null) {
                    _topicsFlow.value = TopicsEvent.Success(result.data)
                } else {
                    _topicsFlow.value = TopicsEvent.Failure("Failed to retrieve data from server")
                }
            }
            is Resource.Error -> _topicsFlow.value = TopicsEvent.Failure(result.msg!!)
        }
    }

    private fun searchTopicPhotos(id: String) {
        if (!isTopicTheSame(id)) {
            photosChannel.offer(id)
            state.set(CURRENT_QUERY, id)
        }
    }

    private fun isTopicTheSame(currentQuery: String) =
        state.get<String>(CURRENT_QUERY).toString() == currentQuery

    fun onTopicSelected(id: String) {
        searchTopicPhotos(id)
    }

    fun onPhotoSelected(photo: Photo) = viewModelScope.launch {
        galleryEventsChannel.send(GalleryEvent.NavigateToPhotoDetailsFragment(photo))
    }

    sealed class GalleryEvent {
        data class NavigateToPhotoDetailsFragment(val photo: Photo) : GalleryEvent()
    }

    sealed class TopicsEvent {
        data class Success(val topics: List<Topic>) : TopicsEvent()
        data class Failure(val errorText: String) : TopicsEvent()
        object Loading : TopicsEvent()
        object Empty : TopicsEvent()
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private var DEFAULT_QUERY = "bo8jQKTaE0Y"
    }
}