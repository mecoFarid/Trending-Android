package com.mecofarid.shared.domain.features.trending.data.source.remote.service

import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.data.datasource.network.NetworkService
import com.mecofarid.shared.domain.features.trending.data.query.GetAllTrendingQuery
import com.mecofarid.shared.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import com.mecofarid.shared.libs.network.client.retrofit.RetrofitService
import io.reactivex.rxjava3.core.Single

class TrendingService(
    private val retrofitService: RetrofitService
): NetworkService<List<TrendingRemoteEntity>> {
    override fun get(query: Query): Single<List<TrendingRemoteEntity>> =
        when (query) {
            is GetAllTrendingQuery -> query.getTrendingList()
            else -> throw UnsupportedOperationException("Get with query type ($query) is not supported")
        }

    override fun put(
        query: Query,
        data: List<TrendingRemoteEntity>
    ): Single<List<TrendingRemoteEntity>> = throw UnsupportedOperationException("Put is not supported")

    private fun GetAllTrendingQuery.getTrendingList(): Single<List<TrendingRemoteEntity>> =
        retrofitService.searchTrending(query).map { it.items }
}
