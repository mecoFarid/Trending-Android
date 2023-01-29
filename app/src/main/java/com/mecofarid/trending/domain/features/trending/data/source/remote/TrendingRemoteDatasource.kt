package com.mecofarid.trending.domain.features.trending.data.source.remote

import com.mecofarid.trending.domain.common.data.Datasource
import com.mecofarid.trending.domain.common.data.Query
import com.mecofarid.trending.domain.features.trending.data.query.GetAllTrendingsQuery
import com.mecofarid.trending.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import com.mecofarid.trending.domain.features.trending.data.source.remote.service.TrendingService

const val TRENDING_QUERY = "language=+sort:stars"

class TrendingRemoteDatasource(
    private val trendingService: TrendingService
): Datasource<List<TrendingRemoteEntity>> {
    override suspend fun get(query: Query): List<TrendingRemoteEntity> =
        when (query) {
            GetAllTrendingsQuery -> getTrendingList()
            else -> throw UnsupportedOperationException("Get with query type ($query) is not supported")
        }

    override suspend fun put(query: Query, data: List<TrendingRemoteEntity>): List<TrendingRemoteEntity> =
        throw UnsupportedOperationException("Put is not supported")

    private suspend fun getTrendingList(): List<TrendingRemoteEntity> =
        trendingService.searchTrending(TRENDING_QUERY).items
}
