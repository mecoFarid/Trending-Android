package com.mecofarid.sadapayallstar.common

interface Datasource<T> {
    suspend fun get(query: Query, operation: Operation): T

    suspend fun put(query: Query, t: T): T
}