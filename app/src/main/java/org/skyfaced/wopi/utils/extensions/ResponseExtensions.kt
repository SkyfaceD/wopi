package org.skyfaced.wopi.utils.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import org.skyfaced.wopi.utils.Response
import org.skyfaced.wopi.utils.result.Result
import org.skyfaced.wopi.utils.result.asSuccess
import org.skyfaced.wopi.utils.result.isFailure

fun <T> success(data: T): Response.Success<T> {
    return Response.Success(data)
}

fun error(cause: Throwable? = null): Response.Error {
    return Response.Error(cause)
}

fun loading() = Response.Loading

fun <T> handle(result: Result<T>): Response<T> =
    if (result.isFailure()) error(result)
    else success(result.asSuccess().value)

fun Response<*>.isLoading() = this is Response.Loading

fun <T> Flow<Response<T>>.onResponse() =
    onStart { emit(loading()) }.catch { cause -> emit(error(cause)) }