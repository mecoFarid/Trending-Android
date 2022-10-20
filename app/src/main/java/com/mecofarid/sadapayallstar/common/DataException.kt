package com.mecofarid.sadapayallstar.common

sealed class DataException(override val cause: Throwable?): Throwable(cause) {
    data class DataNotFoundException(override val cause: Throwable?): DataException(cause)
}
