package com.ragvax.picttr.data.topic.model

import android.os.Parcelable
import com.ragvax.picttr.data.user.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(
    val id: String,
    val slug: String,
    val title: String,
    val description: String?,
    val owners: List<User>?,
) : Parcelable
