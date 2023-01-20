package com.mecofarid.trending.features.repo

import com.mecofarid.trending.common.data.DataException
import com.mecofarid.trending.common.data.Mapper
import com.mecofarid.trending.di.db.DbComponent
import com.mecofarid.trending.di.network.NetworkComponent
import com.mecofarid.trending.features.repo.data.RepoRepository
import com.mecofarid.trending.features.repo.data.mapper.OwnerLocalEntityToOwnerMapper
import com.mecofarid.trending.features.repo.data.mapper.OwnerRemoteEntityToOwnerLocalEntityMapper
import com.mecofarid.trending.features.repo.data.mapper.RepoLocalEntityToRepoMapper
import com.mecofarid.trending.features.repo.data.mapper.RepoRemoteEntityToLocalEntityMapper
import com.mecofarid.trending.features.repo.data.source.local.RepoLocalDatasource
import com.mecofarid.trending.features.repo.data.source.remote.RepoRemoteDatasource
import com.mecofarid.trending.features.repo.domain.interactor.GetRepoInteractor

interface RepoComponent {
    fun getRepoInteractor(): GetRepoInteractor
}

class RepoModule(
    private val dbComponent: DbComponent,
    private val networkComponent: NetworkComponent
): RepoComponent {

    private val repository by lazy {
        val cacheDataSource = RepoLocalDatasource(dbComponent.repoLocalEntityDao())
        val mainDatasource = RepoRemoteDatasource(networkComponent.repoService())

        val toLocalEntityMapper = RepoRemoteEntityToLocalEntityMapper(OwnerRemoteEntityToOwnerLocalEntityMapper())
        val toDomainMapper = RepoLocalEntityToRepoMapper(OwnerLocalEntityToOwnerMapper())

        RepoRepository(
            cacheDataSource,
            mainDatasource,
            toLocalEntityMapper,
            toDomainMapper
        )
    }

    override fun getRepoInteractor(): GetRepoInteractor = GetRepoInteractor(repository)
}