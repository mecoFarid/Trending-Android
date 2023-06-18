package com.mecofarid.shared.domain.features.trending.di

import com.mecofarid.shared.domain.common.data.DataException
import com.mecofarid.shared.domain.common.data.DatasourceMapper
import com.mecofarid.shared.domain.common.data.ListMapper
import com.mecofarid.shared.domain.common.data.Mapper
import com.mecofarid.shared.domain.common.data.NetworkException
import com.mecofarid.shared.domain.common.data.VoidMapper
import com.mecofarid.shared.domain.common.data.datasource.network.NetworkDatasource
import com.mecofarid.shared.domain.common.data.datasource.network.NetworkService
import com.mecofarid.shared.domain.common.data.repository.Repository
import com.mecofarid.shared.domain.common.data.repository.cache.CacheRepository
import com.mecofarid.shared.domain.di.app.IoScheduler
import com.mecofarid.shared.domain.features.trending.data.mapper.OwnerLocalEntityToOwnerMapper
import com.mecofarid.shared.domain.features.trending.data.mapper.OwnerRemoteEntityToOwnerMapper
import com.mecofarid.shared.domain.features.trending.data.mapper.OwnerToOwnerLocalEntityMapper
import com.mecofarid.shared.domain.features.trending.data.mapper.TrendingLocalEntityToTrendingMapper
import com.mecofarid.shared.domain.features.trending.data.mapper.TrendingRemoteEntityToTrendingMapper
import com.mecofarid.shared.domain.features.trending.data.mapper.TrendingToTrendingLocalEntityMapper
import com.mecofarid.shared.domain.features.trending.data.source.local.TrendingLocalDatasource
import com.mecofarid.shared.domain.features.trending.data.source.local.dao.TrendingLocalEntityDao
import com.mecofarid.shared.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import com.mecofarid.shared.domain.features.trending.domain.model.Trending
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.core.Scheduler

@InstallIn(SingletonComponent::class)
@Module
object TrendingModule {

    @Provides
    fun provideTrendingRepository(
        @IoScheduler scheduler: Scheduler,
        trendingService: NetworkService<List<TrendingRemoteEntity>>,
        trendingLocalEntityDao: TrendingLocalEntityDao
    ): Repository<List<Trending>, DataException> {
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
            NetworkDatasource(trendingService, networkExceptionMapper),
            mainOutMapper,
            VoidMapper()
        )
        val cacheDataSource = DatasourceMapper(
            TrendingLocalDatasource(trendingLocalEntityDao),
            cacheOutMapper,
            cacheInMapper
        )

        return CacheRepository(scheduler, cacheDataSource, mainDatasource)
    }
}
