package com.mecofarid.trending.domain.common.data.datasource

import com.mecofarid.trending.domain.common.either.Either
import com.mecofarid.trending.domain.common.data.Query

interface Datasource<T, E> {
    suspend fun get(query: Query): Either<E, T>

    suspend fun put(query: Query, data: T): Either<E, T>
}
