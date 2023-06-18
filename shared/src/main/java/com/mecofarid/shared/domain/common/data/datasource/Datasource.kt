package com.mecofarid.shared.domain.common.data.datasource

import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.either.Either
import io.reactivex.rxjava3.core.Flowable

interface Datasource<T, E> {
    fun get(query: Query): Flowable<Either<E, T>>

    fun put(query: Query, data: T): Flowable<Either<E, T>>
}
