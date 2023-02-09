package com.mecofarid.shared.domain.common.data.datasource

import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.either.Either

interface Datasource<T, E> {
    suspend fun get(query: Query): Either<E, T>

    suspend fun put(query: Query, data: T): Either<E, T>
}
