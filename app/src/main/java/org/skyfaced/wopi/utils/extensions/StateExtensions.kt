package org.skyfaced.wopi.utils.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import org.skyfaced.wopi.utils.State
import org.skyfaced.wopi.utils.result.Result
import org.skyfaced.wopi.utils.result.asSuccess
import org.skyfaced.wopi.utils.result.isFailure

fun <T> success(data: T): State.Success<T> {
    return State.Success(data)
}

fun error(cause: Throwable? = null): State.Error {
    return State.Error(cause)
}

fun loading() = State.Loading

fun <T> handle(result: Result<T>): State<T> =
    if (result.isFailure()) error(result)
    else success(result.asSuccess().value)

fun State<*>.isLoading() = this is State.Loading

fun <T> Flow<State<T>>.onResponse() =
    onStart { emit(loading()) }.catch { cause -> emit(error(cause)) }