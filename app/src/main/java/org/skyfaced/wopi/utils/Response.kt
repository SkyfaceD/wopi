package org.skyfaced.wopi.utils

sealed class Response<out T> {
    data class Success<T>(val data: T) : Response<T>()

    data class Error(val message: String?, val cause: Throwable?) : Response<Nothing>()

    object Load : Response<Nothing>()
}