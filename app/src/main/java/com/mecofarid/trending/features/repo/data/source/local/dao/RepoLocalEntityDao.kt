package com.mecofarid.trending.features.repo.data.source.local.dao

import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity

interface RepoLocalEntityDao {

    suspend fun getAllTrendingRepos(): List<RepoLocalEntity>

    suspend fun deleteAllTrendingReposAndInsert(repos: List<RepoLocalEntity>)
}