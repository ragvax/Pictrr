package com.ragvax.picttr.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val profileEventChannel = Channel<ProfileEvent>(Channel.CONFLATED)
    val profileEvent = profileEventChannel.receiveAsFlow()

    fun onLocationClick(location: String?) = viewModelScope.launch {
        profileEventChannel.send(ProfileEvent.NavigateToMaps(location))
    }

    sealed class ProfileEvent {
        data class NavigateToMaps(val location: String?) : ProfileEvent()
    }
}