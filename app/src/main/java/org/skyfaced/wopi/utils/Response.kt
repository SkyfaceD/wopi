package org.skyfaced.wopi.utils

import org.skyfaced.wopi.utils.ResponseMessage.Companion.toMessage
import org.skyfaced.wopi.utils.result.Result
import org.skyfaced.wopi.utils.result.asHttpError
import org.skyfaced.wopi.utils.result.isHttpError

sealed class Response<out T> {
    data class Success<T>(val data: T) : Response<T>()

    data class Error(val message: String?, val cause: Throwable?) : Response<Nothing>() {
        //@formatter:off
        constructor(failure: Result.Failure<*>) : this(
            message =
                if (failure.isHttpError()) failure.asHttpError().statusCode.toMessage()
                else null,
            cause = failure.error
        )
        //@formatter:on
    }

    object Loading : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success(data=$data)"
            is Error -> "Error(message=${message}, cause=$cause)"
            Loading -> "Loading"
        }
    }
}