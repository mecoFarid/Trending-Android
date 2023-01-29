package com.mecofarid.trending.domain.features.trending.data.source.local

import com.mecofarid.trending.domain.common.data.DataException
import com.mecofarid.trending.domain.common.data.Datasource
import com.mecofarid.trending.domain.common.data.Query
import com.mecofarid.trending.domain.features.trending.data.query.GetAllTrendingsQuery
import com.mecofarid.trending.domain.features.trending.data.source.local.dao.TrendingLocalEntityDao
import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity

class TrendingLocalDatasource(
    private val trendingLocalEntityDao: TrendingLocalEntityDao
): Datasource<List<TrendingLocalEntity>> {
    override suspend fun get(query: Query): List<TrendingLocalEntity> = when (query) {
        GetAllTrendingsQuery -> getTrendingsOrThrow()
        else -> throw UnsupportedOperationException("Get with query type ($query) is not supported")
    }

    override suspend fun put(query: Query, data: List<TrendingLocalEntity>): List<TrendingLocalEntity> =
        when (query) {
            GetAllTrendingsQuery -> data.apply {
                trendingLocalEntityDao.deleteAllTrendingAndInsert(this)
            }
            else -> throw UnsupportedOperationException("Put with query type ($query) is not supported")
        }

    private suspend fun getTrendingsOrThrow() = trendingLocalEntityDao.getAllTrendings().also {
        if (it.isEmpty())
            throw DataException.DataNotFoundException()
    }
}
