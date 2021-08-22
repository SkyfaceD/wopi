package org.skyfaced.wopi.utils.extensions

import org.skyfaced.wopi.utils.Response

fun <T> success(data: T): Response.Success<T> {
    return Response.Success(data)
}

fun error(message: String? = null, cause: Throwable? = null): Response.Error {
    return Response.Error(message, cause)
}

fun load() = Response.Load