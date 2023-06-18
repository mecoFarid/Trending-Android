package com.mecofarid.shared.domain.features.trending.data.source.local

import com.mecofarid.shared.domain.common.data.DataException
import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.data.datasource.Datasource
import com.mecofarid.shared.domain.common.either.Either
import com.mecofarid.shared.domain.features.trending.data.TrendingResult
import com.mecofarid.shared.domain.features.trending.data.query.GetAllTrendingQuery
import com.mecofarid.shared.domain.features.trending.data.source.local.dao.TrendingLocalEntityDao
import com.mecofarid.shared.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import io.reactivex.rxjava3.core.Flowable

class TrendingLocalDatasource(
    private val trendingLocalEntityDao: TrendingLocalEntityDao
): Datasource<List<TrendingLocalEntity>, DataException> {

    override fun get(query: Query): Flowable<TrendingResult<TrendingLocalEntity>> = when (query) {
        is GetAllTrendingQuery -> getAllTrending()
        else -> throw UnsupportedOperationException("Get with query type ($query) is not supported")
    }

    override fun put(
        query: Query,
        data: List<TrendingLocalEntity>
    ): Flowable<TrendingResult<TrendingLocalEntity>> =
        when (query) {
            is GetAllTrendingQuery -> {
                trendingLocalEntityDao.deleteAllTrendingAndInsert(data)
                Flowable.just(Either.Right(data))
            }

            else -> throw UnsupportedOperationException("Put with query type ($query) is not supported")
        }

    private fun getAllTrending(): Flowable<TrendingResult<TrendingLocalEntity>> {
        val data = trendingLocalEntityDao.getAllTrendings()
        return data.flatMap {
            val result =
                if (it.isEmpty())
                    Either.Left(DataException.DataNotFoundException())
                else
                    Either.Right(it)

            Flowable.just(result)
        }
    }
}
