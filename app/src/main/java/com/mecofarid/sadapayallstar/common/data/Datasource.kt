package com.mecofarid.sadapayallstar.common.data

interface Datasource<T> {
    suspend fun get(query: Query, operation: Operation): T

    suspend fun put(query: Query, data: T): T
}