package com.mecofarid.shared.domain.common.data.repository.cache

import com.mecofarid.shared.domain.common.data.Operation
import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.data.datasource.Datasource
import com.mecofarid.shared.domain.common.data.repository.Repository
import com.mecofarid.shared.domain.common.either.Either
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler

class CacheRepository<T, E>(
    private val scheduler: Scheduler,
    private val cacheDatasource: Datasource<T, E>,
    private val mainDatasource: Datasource<T, E>
) : Repository<T, E> {

    override fun get(query: Query, operation: Operation): Flowable<Either<E, T>> =
        when (operation) {
            Operation.SyncMainOperation -> getSyncedData(query)
            Operation.CacheElseSyncMainOperation -> getCachedElseSyncedData(query)
        }.subscribeOn(scheduler)

    private fun getSyncedData(query: Query): Flowable<Either<E, T>> =
        mainDatasource.get(query)
            .flatMap {
                it.onRight {
                    cacheDatasource.put(query, it)
                }
                getCachedData(query)
            }


    private fun getCachedElseSyncedData(query: Query): Flowable<Either<E, T>> =
        getCachedData(query)
            .flatMap {
                if (it.isLeft())
                    get(query, Operation.SyncMainOperation)
                else
                    Flowable.just(it)
            }

    private fun getCachedData(query: Query): Flowable<Either<E, T>> =
        cacheDatasource.get(query)
}
