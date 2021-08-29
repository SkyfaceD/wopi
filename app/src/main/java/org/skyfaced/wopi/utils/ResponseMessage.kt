package org.skyfaced.wopi.utils

import java.net.HttpURLConnection

/**
 * All names must be unique
 */
enum class ResponseMessage {
    HttpForbidden,
    Unexpected;

    companion object {
        //TODO Fill code errors
        fun Int.toMessage(): String {
            return when (this) {
                HttpURLConnection.HTTP_FORBIDDEN -> HttpForbidden.name
                else -> Unexpected.name
            }
        }
    }
}