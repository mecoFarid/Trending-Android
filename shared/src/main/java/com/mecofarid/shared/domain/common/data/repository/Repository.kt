package com.mecofarid.shared.domain.common.data.repository

import com.mecofarid.shared.domain.common.data.Operation
import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.either.Either
import io.reactivex.rxjava3.core.Flowable

interface Repository<T, E> {
    fun get(query: Query, operation: Operation): Flowable<Either<E, T>>
}
