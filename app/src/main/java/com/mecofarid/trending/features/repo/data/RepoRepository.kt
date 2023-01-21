package com.mecofarid.trending.features.repo.data

import com.mecofarid.trending.common.data.Datasource
import com.mecofarid.trending.common.data.Mapper
import com.mecofarid.trending.common.data.Repository
import com.mecofarid.trending.common.data.Query
import com.mecofarid.trending.common.data.Operation
import com.mecofarid.trending.common.data.DataException.DataNotFoundException
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity
import com.mecofarid.trending.features.repo.data.source.remote.entity.RepoRemoteEntity
import com.mecofarid.trending.features.repo.domain.model.Repo

class RepoRepository(
    private val cacheDatasource: Datasource<List<RepoLocalEntity>>,
    private val mainDatasource: Datasource<List<RepoRemoteEntity>>,
    private val toLocalEntityMapper: Mapper<RepoRemoteEntity, RepoLocalEntity>,
    private val toDomainMapper: Mapper<RepoLocalEntity, Repo>
) : Repository<List<Repo>>{

    override suspend fun get(query: Query, operation: Operation): List<Repo> {
        return when (operation) {
            Operation.SyncMainOperation -> getSyncedData(query)
            Operation.CacheElseSyncMainOperation -> getCachedElseSyncedData(query)
        }
    }

    private suspend fun getSyncedData(query: Query) : List<Repo>{
        return try {
            val data = mainDatasource.get(query).map {
                toLocalEntityMapper.map(it)
            }
            val cachedData = cacheDatasource.put(query, data)
            cachedData.map { toDomainMapper.map(it) }
        } catch (ignored: DataNotFoundException) {
            getCachedData(query)
        }
    }

    private suspend fun getCachedElseSyncedData(query: Query): List<Repo>{
        return try {
            getCachedData(query)
        } catch (ignored: DataNotFoundException) {
            get(query, Operation.SyncMainOperation)
        }
    }

    private suspend fun getCachedData(query: Query): List<Repo> {
        val cachedData = cacheDatasource.get(query)
        return cachedData.map { toDomainMapper.map(it) }
    }
}
