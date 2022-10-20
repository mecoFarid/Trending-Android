package com.mecofarid.sadapayallstar.features.repo.data.source.remote

import com.mecofarid.sadapayallstar.common.data.DataException
import com.mecofarid.sadapayallstar.common.data.Datasource
import com.mecofarid.sadapayallstar.common.data.Operation
import com.mecofarid.sadapayallstar.common.data.Query
import com.mecofarid.sadapayallstar.features.repo.data.query.GetAllStarredReposQuery
import com.mecofarid.sadapayallstar.features.repo.data.source.remote.entity.RepoRemoteEntity
import com.mecofarid.sadapayallstar.features.repo.data.source.remote.service.RepoService

class RepoRemoteDatasource(private val repoService: RepoService): Datasource<List<RepoRemoteEntity>> {
    override suspend fun get(query: Query, operation: Operation): List<RepoRemoteEntity> =
        when (query) {
            GetAllStarredReposQuery -> repoService.searchRepos("language=+sort:stars").items ?: throw DataException.DataNotFoundException()
            else -> throw UnsupportedOperationException("Get with query type ($query) is not supported")
        }

    override suspend fun put(query: Query, data: List<RepoRemoteEntity>): List<RepoRemoteEntity> =
        throw UnsupportedOperationException("Put is not supported")
}