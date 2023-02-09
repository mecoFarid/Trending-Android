package com.mecofarid.shared.domain.common.data.repository

import com.mecofarid.shared.domain.common.data.Operation
import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.either.Either

interface Repository<T, E> {
    suspend fun get(query: Query, operation: Operation): Either<E, T>
}
