package com.mecofarid.sadapayallstar.features.repo.data.source.local

import com.mecofarid.sadapayallstar.common.data.Datasource
import com.mecofarid.sadapayallstar.common.data.Operation
import com.mecofarid.sadapayallstar.common.data.Query
import com.mecofarid.sadapayallstar.features.repo.data.query.GetAllStarredReposQuery
import com.mecofarid.sadapayallstar.features.repo.data.source.local.dao.RepoLocalEntityDao
import com.mecofarid.sadapayallstar.features.repo.data.source.local.entity.RepoLocalEntity

class RepoLocalDatasource(
    private val repoLocalEntityDao: RepoLocalEntityDao
): Datasource<List<RepoLocalEntity>> {
    override suspend fun get(query: Query, operation: Operation): List<RepoLocalEntity> = when (query) {
        GetAllStarredReposQuery -> repoLocalEntityDao.getAllStarredRepos()
        else -> throw UnsupportedOperationException("Get with query type ($query) is not supported")
    }

    override suspend fun put(query: Query, data: List<RepoLocalEntity>): List<RepoLocalEntity> = when (query) {
        GetAllStarredReposQuery -> data.apply {
            repoLocalEntityDao.deleteAllStarredReposAndInsert(this)
        }
        else -> throw UnsupportedOperationException("Put with query type ($query) is not supported")
    }
}