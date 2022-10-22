package com.mecofarid.trending.common.data

sealed class NetworkException(override val cause: Throwable? = null): Throwable(cause) {
    data class HttpException(override val cause: Throwable? = null): NetworkException(cause)
    data class ConnectionException(override val cause: Throwable? = null): NetworkException(cause)
}
