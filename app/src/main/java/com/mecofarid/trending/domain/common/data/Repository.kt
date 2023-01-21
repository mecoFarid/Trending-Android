package com.mecofarid.trending.domain.common.data

interface Repository<T> {
    suspend fun get(query: Query, operation: Operation): T
}
