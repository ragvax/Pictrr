package com.ragvax.picttr.ui.photodetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ragvax.picttr.data.user.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoZoomViewModel @Inject constructor() : ViewModel() {

    private val photoZoomEventChannel = Channel<PhotoZoomEvent>(Channel.CONFLATED)
    val photoZoomEvent = photoZoomEventChannel.receiveAsFlow()

    fun onUserClick(user: User?) = viewModelScope.launch {
        photoZoomEventChannel.send(PhotoZoomEvent.NavigateToUserProfile(user))
    }

    sealed class PhotoZoomEvent {
        data class NavigateToUserProfile(val user: User?) : PhotoZoomEvent()
    }
}
