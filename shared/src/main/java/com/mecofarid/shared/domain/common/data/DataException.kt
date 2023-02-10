package com.mecofarid.shared.domain.common.data

sealed class DataException(override val cause: Throwable? = null): Throwable(cause) {
    data class DataNotFoundException(
        override val cause: Throwable? = null
    ): DataException(cause)
}
