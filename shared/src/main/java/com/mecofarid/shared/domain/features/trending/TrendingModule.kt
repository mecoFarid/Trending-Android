package com.mecofarid.shared.domain.features.trending

import com.mecofarid.shared.domain.common.data.DataException
import com.mecofarid.shared.domain.common.data.DatasourceMapper
import com.mecofarid.shared.domain.common.data.ListMapper
import com.mecofarid.shared.domain.common.data.Mapper
import com.mecofarid.shared.domain.common.data.NetworkException
import com.mecofarid.shared.domain.common.data.VoidMapper
import com.mecofarid.shared.domain.common.data.datasource.network.NetworkDatasource
import com.mecofarid.shared.domain.common.data.repository.cache.CacheRepository
import com.mecofarid.shared.domain.features.trending.data.mapper.OwnerLocalEntityToOwnerMapper
import com.mecofarid.shared.domain.features.trending.data.mapper.OwnerRemoteEntityToOwnerMapper
import com.mecofarid.shared.domain.features.trending.data.mapper.OwnerToOwnerLocalEntityMapper
import com.mecofarid.shared.domain.features.trending.data.mapper.TrendingLocalEntityToTrendingMapper
import com.mecofarid.shared.domain.features.trending.data.mapper.TrendingRemoteEntityToTrendingMapper
import com.mecofarid.shared.domain.features.trending.data.mapper.TrendingToTrendingLocalEntityMapper
import com.mecofarid.shared.domain.features.trending.data.source.local.TrendingLocalDatasource
import com.mecofarid.shared.domain.features.trending.domain.interactor.GetTrendingInteractor
import com.mecofarid.shared.ui.trending.TrendingViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val trendingModule = module {
    factory {
        val networkExceptionMapper = object : Mapper<NetworkException, DataException> {
            override fun map(input: NetworkException) = DataException.DataNotFoundException(input)
        }

        val mainOutMapper =
            ListMapper(TrendingRemoteEntityToTrendingMapper(OwnerRemoteEntityToOwnerMapper()))
        val cacheOutMapper =
            ListMapper(TrendingLocalEntityToTrendingMapper(OwnerLocalEntityToOwnerMapper()))

        val cacheInMapper =
            ListMapper(TrendingToTrendingLocalEntityMapper(OwnerToOwnerLocalEntityMapper()))

        val mainDatasource = DatasourceMapper(
            NetworkDatasource(service = get(), networkExceptionMapper),
            mainOutMapper,
            VoidMapper()
        )
        val cacheDataSource = DatasourceMapper(
            TrendingLocalDatasource(trendingLocalEntityDao = get()),
            cacheOutMapper,
            cacheInMapper
        )

        GetTrendingInteractor(CacheRepository(cacheDataSource, mainDatasource))
    }
    viewModelOf(::TrendingViewModel)
}
