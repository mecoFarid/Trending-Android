package com.mecofarid.trending.domain.features.trending

import com.mecofarid.trending.domain.di.db.DbComponent
import com.mecofarid.trending.domain.di.network.NetworkComponent
import com.mecofarid.trending.domain.features.trending.data.TrendingRepository
import com.mecofarid.trending.domain.features.trending.data.mapper.OwnerLocalEntityToOwnerMapper
import com.mecofarid.trending.domain.features.trending.data.mapper.OwnerRemoteEntityToOwnerLocalEntityMapper
import com.mecofarid.trending.domain.features.trending.data.mapper.TrendingLocalEntityToTrendingMapper
import com.mecofarid.trending.domain.features.trending.data.mapper.TrendingRemoteEntityToLocalTrendingEntityMapper
import com.mecofarid.trending.domain.features.trending.data.source.local.TrendingLocalDatasource
import com.mecofarid.trending.domain.features.trending.data.source.remote.TrendingRemoteDatasource
import com.mecofarid.trending.domain.features.trending.domain.interactor.GetTrendingInteractor

interface TrendingComponent {
    fun getTrendingInteractor(): GetTrendingInteractor
}

class TrendingModule(
    private val dbComponent: DbComponent,
    private val networkComponent: NetworkComponent
): TrendingComponent {

    private val repository by lazy {
        val cacheDataSource = TrendingLocalDatasource(dbComponent.trendingLocalEntityDao())
        val mainDatasource = TrendingRemoteDatasource(networkComponent.trendingService())

        val toLocalEntityMapper = TrendingRemoteEntityToLocalTrendingEntityMapper(
            OwnerRemoteEntityToOwnerLocalEntityMapper()
        )
        val toDomainMapper = TrendingLocalEntityToTrendingMapper(OwnerLocalEntityToOwnerMapper())

        TrendingRepository(
            cacheDataSource,
            mainDatasource,
            toLocalEntityMapper,
            toDomainMapper
        )
    }

    override fun getTrendingInteractor(): GetTrendingInteractor = GetTrendingInteractor(repository)
}
