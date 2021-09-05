package org.skyfaced.wopi.utils

sealed class Response<out T> {
    data class Success<T>(val data: T) : Response<T>()

    data class Error(val cause: Throwable?) : Response<Nothing>()

    object Loading : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success(data=$data)"
            is Error -> "Error(cause=$cause)"
            is Loading -> "Loading"
        }
    }
}