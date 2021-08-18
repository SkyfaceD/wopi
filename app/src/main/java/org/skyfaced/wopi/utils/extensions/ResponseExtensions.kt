package org.skyfaced.wopi.utils.extensions

import org.skyfaced.wopi.utils.Response

fun <T> success(data: T): Response.Success<T> {
    return Response.Success(data)
}

fun error(message: String?, cause: Throwable?): Response.Error {
    return Response.Error(message, cause)
}

fun load() = Response.Load

fun <T> handle(data: T): Response<T> = try {
    success(data)
} catch (e: Exception) {
    error(e.message, e)
}