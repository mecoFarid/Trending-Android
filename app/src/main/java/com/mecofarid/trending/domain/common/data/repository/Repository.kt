package com.mecofarid.trending.domain.common.data.repository

import com.mecofarid.trending.domain.common.either.Either
import com.mecofarid.trending.domain.common.data.Operation
import com.mecofarid.trending.domain.common.data.Query

interface Repository<T, E> {
    suspend fun get(query: Query, operation: Operation): Either<E, T>
}
