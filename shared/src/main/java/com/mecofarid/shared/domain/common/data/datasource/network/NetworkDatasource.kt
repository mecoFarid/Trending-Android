package com.mecofarid.shared.domain.common.data.datasource.network

import com.mecofarid.shared.domain.common.data.Mapper
import com.mecofarid.shared.domain.common.data.NetworkException
import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.data.datasource.Datasource
import com.mecofarid.shared.domain.common.either.Either
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class NetworkDatasource<T: Any, E>(
    private val service: NetworkService<T>,
    private val exceptionMapper: Mapper<NetworkException, E>
): Datasource<T, E> {

    override fun get(query: Query): Flowable<Either<E, T>> =
        executeRequest { service.get(query) }

    override fun put(query: Query, data: T): Flowable<Either<E, T>> =
        executeRequest { service.put(query, data) }

    private inline fun <T : Any> executeRequest(block: () -> Single<T>): Flowable<Either<E, T>> =
        block()
            .map<Either<E, T>> {
                Either.Right(it)
            }.onSafeErrorResumeNext<Either<E, T>, NetworkException> {
                Single.just(Either.Left(exceptionMapper.map(it)))
            }.toFlowable()

}

private inline fun <T: Any, reified E> Single<T>.onSafeErrorResumeNext(
    crossinline fallbackSupplier: (E) -> Single<T>
): Single<T> = onErrorResumeNext {
    if (it is E)
        fallbackSupplier(it)
    else
        Single.error(it)
}
