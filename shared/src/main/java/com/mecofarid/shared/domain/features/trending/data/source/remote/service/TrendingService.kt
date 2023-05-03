package com.mecofarid.shared.domain.features.trending.data.source.remote.service

import com.mecofarid.shared.GetTrendingQuery
import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.data.datasource.network.NetworkService
import com.mecofarid.shared.domain.features.trending.data.query.GetAllTrendingQuery
import com.mecofarid.shared.fragment.TrendingEntity
import com.mecofarid.shared.libs.network.client.NetworkClient
import com.mecofarid.shared.type.SearchType
import javax.inject.Inject

typealias TrendingRemoteEntity = TrendingEntity
typealias OwnerRemoteEntity = TrendingEntity.Owner

class TrendingService @Inject constructor(
    private val networkClient: NetworkClient,
): NetworkService<List<@JvmSuppressWildcards TrendingRemoteEntity>> {
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
        networkClient
            .query(GetTrendingQuery(query = query, type = SearchType.REPOSITORY, first = first))
            .search.repositories.map { it.trendingEntity }
}
