package com.mecofarid.trending.features.repo.data.source.local

import com.mecofarid.trending.common.data.Datasource
import com.mecofarid.trending.common.data.Operation
import com.mecofarid.trending.common.data.Query
import com.mecofarid.trending.features.repo.data.query.GetAllTrendingReposQuery
import com.mecofarid.trending.features.repo.data.source.local.dao.RepoLocalEntityDao
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity

class RepoLocalDatasource(
    private val repoLocalEntityDao: RepoLocalEntityDao
): Datasource<List<RepoLocalEntity>> {
    override suspend fun get(query: Query, operation: Operation): List<RepoLocalEntity> = when (query) {
        GetAllTrendingReposQuery -> repoLocalEntityDao.getAllTrendingRepos()
        else -> throw UnsupportedOperationException("Get with query type ($query) is not supported")
    }

    override suspend fun put(query: Query, data: List<RepoLocalEntity>): List<RepoLocalEntity> = when (query) {
        GetAllTrendingReposQuery -> data.apply {
            repoLocalEntityDao.deleteAllTrendingReposAndInsert(this)
        }
        else -> throw UnsupportedOperationException("Put with query type ($query) is not supported")
    }
}