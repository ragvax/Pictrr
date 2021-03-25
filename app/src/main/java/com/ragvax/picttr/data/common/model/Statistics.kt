package com.ragvax.picttr.data.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoStatistics(
    val id: String,
    val downloads: Downloads,
    val views: Views,
    val likes: Likes,
) : Parcelable

@Parcelize
data class UserStatistics(
    val username: String,
    val downloads: Downloads,
    val views: Views,
    val likes: Likes,
) : Parcelable

@Parcelize
data class Downloads(
    val total: Int,
    val historical: Historical,
) : Parcelable

@Parcelize
data class Views(
    val total: Int,
    val historical: Historical,
) : Parcelable

@Parcelize
data class Likes(
    val total: Int,
    val historical: Historical,
) : Parcelable

@Parcelize
data class Historical(
    val change: Int,
    val resolution: String,
    val quality: String,
    val values: List<Value>,
) : Parcelable

@Parcelize
data class Value(
    val date: String,
    val value: Int
) : Parcelable