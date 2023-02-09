package com.mecofarid.trending.domain.common.data.datasource.network

import com.mecofarid.trending.domain.common.data.datasource.Datasource
import com.mecofarid.trending.domain.common.either.Either
import com.mecofarid.trending.domain.common.either.asEither
import com.mecofarid.trending.domain.common.data.Mapper
import com.mecofarid.trending.domain.common.data.NetworkException
import com.mecofarid.trending.domain.common.data.Query

class NetworkDatasource<T, E>(
    private val service: NetworkService<T>,
    private val exceptionMapper: Mapper<NetworkException, E>
): Datasource<T, E> {

    override suspend fun get(query: Query): Either<E, T> =
        executeRequest{ service.get(query) }

    override suspend fun put(query: Query, data: T): Either<E, T> =
        executeRequest { service.put(query, data) }

    private inline fun <T> executeRequest(block: () -> T): Either<E, T> =
        asEither<NetworkException, T> {
            block()
        }.mapLeft {
            exceptionMapper.map(it)
        }
}
