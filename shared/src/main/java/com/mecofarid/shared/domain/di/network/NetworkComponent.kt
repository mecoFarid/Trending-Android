package com.mecofarid.shared.domain.di.network

import com.mecofarid.shared.domain.common.data.datasource.network.NetworkService
import com.mecofarid.shared.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity

interface NetworkComponent {
    fun trendingService(): NetworkService<List<TrendingRemoteEntity>>
}
