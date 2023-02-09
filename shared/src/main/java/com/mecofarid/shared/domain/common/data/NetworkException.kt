package com.mecofarid.shared.domain.common.data

sealed class NetworkException(override val cause: Throwable? = null): com.mecofarid.shared.domain.common.data.DataException(cause) {
    data class HttpException(val code: Int): NetworkException()
    data class ConnectionException(override val cause: Throwable? = null): NetworkException(cause)
}
