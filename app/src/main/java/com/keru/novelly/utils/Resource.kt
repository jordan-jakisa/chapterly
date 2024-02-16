package com.keru.novelly.utils

sealed class Resource<T>(val message: String? = null, val data: T? = null) {
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(message = message, data = data)
}