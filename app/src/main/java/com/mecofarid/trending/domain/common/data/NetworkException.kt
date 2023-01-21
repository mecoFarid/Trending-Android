package com.mecofarid.trending.domain.common.data

sealed class NetworkException(override val cause: Throwable? = null): Throwable(cause) {
    data class HttpException(val code: Int): NetworkException()
    data class ConnectionException(override val cause: Throwable? = null): NetworkException(cause)
}
