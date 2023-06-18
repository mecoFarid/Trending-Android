package com.mecofarid.shared.domain.common.data

import com.mecofarid.shared.domain.common.data.datasource.Datasource
import com.mecofarid.shared.domain.common.either.Either
import io.reactivex.rxjava3.core.Flowable

class DatasourceMapper<I, O, E: DataException>(
    private val datasource: Datasource<I, E>,
    private val outMapper: Mapper<I, O>,
    private val inMapper: Mapper<O, I>
): Datasource<O, E> {

    override fun get(query: Query): Flowable<Either<E, O>> =
        datasource.get(query).map {
            it.map { data ->
                outMapper.map(data)
            }
        }

    override fun put(query: Query, data: O): Flowable<Either<E, O>> =
        datasource.put(query, inMapper.map(data)).map {
            it.map { data ->
                outMapper.map(data)
            }
        }
}
