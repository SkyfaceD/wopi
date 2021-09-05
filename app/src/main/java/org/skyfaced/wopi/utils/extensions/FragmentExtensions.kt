package org.skyfaced.wopi.utils.extensions

import androidx.fragment.app.Fragment
import org.skyfaced.wopi.R
import org.skyfaced.wopi.utils.result.HttpException
import java.net.HttpURLConnection

fun Fragment.handleNetworkException(cause: HttpException): String {
    return when (cause.statusCode) {
        HttpURLConnection.HTTP_FORBIDDEN -> getString(R.string.error_forbidden_403)
        else -> getString(R.string.error_unhandled_network)
    }
}