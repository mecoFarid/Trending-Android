package com.mecofarid.sadapayallstar.features.repo.data

import com.mecofarid.sadapayallstar.common.Datasource
import com.mecofarid.sadapayallstar.common.Mapper
import com.mecofarid.sadapayallstar.common.Query
import com.mecofarid.sadapayallstar.common.Operation
import com.mecofarid.sadapayallstar.common.DataException.DataNotFoundException
import com.mecofarid.sadapayallstar.features.repo.data.local.entity.RepoLocalEntity
import com.mecofarid.sadapayallstar.features.repo.data.remote.entity.RepoRemoteEntity
import com.mecofarid.sadapayallstar.features.repo.domain.model.Repo

class RepoRepository(
    private val cacheDatasource: Datasource<List<RepoLocalEntity>>,
    private val mainDatasource: Datasource<List<RepoRemoteEntity>>,
    private val toLocalEntityMapper: Mapper<List<RepoRemoteEntity>, List<RepoLocalEntity>>,
    private val toDomainMapper: Mapper<List<RepoLocalEntity>, List<Repo>>
) {
    suspend operator fun invoke(query: Query, operation: Operation): List<Repo> {
        return when (operation) {
            Operation.SyncMainOperation -> getSyncedData(query, operation)
            Operation.CacheElseSyncMainOperation -> getCachedElseSyncedData(query, operation)
        }
    }

    private suspend fun getSyncedData(query: Query, operation: Operation) : List<Repo>{
        return try {
            val data = mainDatasource.get(query, operation)
            val cachedData = cacheDatasource.put(query, toLocalEntityMapper.map(data))
            toDomainMapper.map(cachedData)
        } catch (ignored: DataNotFoundException) {
            getCachedData(query, operation)
        }
    }

    private suspend fun getCachedElseSyncedData(query: Query, operation: Operation): List<Repo>{
        return try {
            getCachedData(query, operation)
        } catch (ignored: DataNotFoundException) {
            invoke(query, Operation.SyncMainOperation)
        }
    }

    private suspend fun getCachedData(query: Query, operation: Operation): List<Repo> {
        val cachedData = cacheDatasource.get(query, operation)
        return toDomainMapper.map(cachedData)
    }
}