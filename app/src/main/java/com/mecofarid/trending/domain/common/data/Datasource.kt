package com.mecofarid.trending.domain.common.data

interface Datasource<T> {
    suspend fun get(query: Query): T

    suspend fun put(query: Query, data: T): T
}