package com.mecofarid.trending.domain.features.repo.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mecofarid.trending.domain.features.repo.data.source.local.entity.RepoLocalEntity

@Dao
interface RepoLocalEntityDao {

    @Query("SELECT * FROM repolocalentity")
    suspend fun getAllTrendingRepos(): List<RepoLocalEntity>

    @Transaction
    suspend fun deleteAllTrendingReposAndInsert(repos: List<RepoLocalEntity>) {
        deleteAll()
        insertAll(repos)
    }

    @Query("DELETE FROM repolocalentity")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(users: List<RepoLocalEntity>)
}
