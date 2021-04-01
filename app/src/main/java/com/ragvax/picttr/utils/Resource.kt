package com.ragvax.picttr.utils

sealed class Resource<T>(val data: T?, val msg: String?) {
    class Success<T>(data: T) : Resource<T>(data, null)
    class Error<T>(message: String?) : Resource<T>(null, message)
}