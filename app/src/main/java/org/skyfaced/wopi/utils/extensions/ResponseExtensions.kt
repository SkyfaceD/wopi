package org.skyfaced.wopi.utils.extensions

import org.skyfaced.wopi.utils.Response
import org.skyfaced.wopi.utils.result.Result
import org.skyfaced.wopi.utils.result.asSuccess
import org.skyfaced.wopi.utils.result.isFailure

fun <T> success(data: T): Response.Success<T> {
    return Response.Success(data)
}

fun error(message: String? = null, cause: Throwable? = null): Response.Error {
    return Response.Error(message, cause)
}

fun error(failure: Result.Failure<*>): Response.Error {
    return Response.Error(failure)
}

fun loading() = Response.Loading

fun <T> handle(result: Result<T>): Response<T> =
    if (result.isFailure()) error(result)
    else success(result.asSuccess().value)

fun Response<*>.isLoading() = this is Response.Loading