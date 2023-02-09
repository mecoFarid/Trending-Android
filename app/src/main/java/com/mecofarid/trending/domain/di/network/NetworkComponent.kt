package com.mecofarid.trending.domain.di.network

import com.mecofarid.trending.domain.common.data.datasource.network.NetworkService
import com.mecofarid.trending.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity

interface NetworkComponent {
    fun trendingService(): NetworkService<List<TrendingRemoteEntity>>
}
