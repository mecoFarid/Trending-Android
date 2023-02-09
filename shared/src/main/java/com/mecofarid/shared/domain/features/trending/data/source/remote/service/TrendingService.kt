package com.mecofarid.shared.domain.features.trending.data.source.remote.service

import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.data.datasource.network.NetworkService
import com.mecofarid.shared.domain.features.trending.data.query.GetAllTrendingQuery
import com.mecofarid.shared.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import com.mecofarid.shared.libs.network.client.retrofit.RetrofitService

class TrendingService(
    private val retrofitService: RetrofitService
): NetworkService<List<TrendingRemoteEntity>> {
    override suspend fun get(query: Query): List<TrendingRemoteEntity> =
        when (query) {
            is GetAllTrendingQuery -> query.getTrendingList()
            else -> throw UnsupportedOperationException("Get with query type ($query) is not supported")
        }

    override suspend fun put(
        query: Query,
        data: List<TrendingRemoteEntity>
    ): List<TrendingRemoteEntity> = throw UnsupportedOperationException("Put is not supported")

    private suspend fun GetAllTrendingQuery.getTrendingList(): List<TrendingRemoteEntity> =
        retrofitService.searchTrending(query).items
}
