package com.mecofarid.trending.features.repo.data.source.remote

import com.mecofarid.trending.common.data.Datasource
import com.mecofarid.trending.common.data.Mapper
import com.mecofarid.trending.common.data.NetworkException
import com.mecofarid.trending.common.data.Query
import com.mecofarid.trending.features.repo.data.query.GetAllTrendingReposQuery
import com.mecofarid.trending.features.repo.data.source.remote.entity.RepoRemoteEntity
import com.mecofarid.trending.features.repo.data.source.remote.service.RepoService

const val TRENDING_REPOS_QUERY = "language=+sort:stars"

class RepoRemoteDatasource(
    private val repoService: RepoService,
    private val exceptionMapper: Mapper<Throwable, Throwable>
): Datasource<List<RepoRemoteEntity>> {
    override suspend fun get(query: Query): List<RepoRemoteEntity> =
        when (query) {
            GetAllTrendingReposQuery -> getTrendingRepos()
            else -> throw UnsupportedOperationException("Get with query type ($query) is not supported")
        }

    override suspend fun put(query: Query, data: List<RepoRemoteEntity>): List<RepoRemoteEntity> =
        throw UnsupportedOperationException("Put is not supported")

    private suspend fun getTrendingRepos() =
        try {
            repoService.searchRepos(TRENDING_REPOS_QUERY).items
        } catch (e: NetworkException) {
            throw exceptionMapper.map(e)
        }
}