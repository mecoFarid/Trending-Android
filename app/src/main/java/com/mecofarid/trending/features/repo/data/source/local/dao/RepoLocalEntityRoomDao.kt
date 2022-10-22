package com.mecofarid.trending.features.repo.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity

@Dao
interface RepoLocalEntityRoomDao: RepoLocalEntityDao {

    @Query("SELECT * FROM repolocalentity")
    override suspend fun getAllTrendingRepos(): List<RepoLocalEntity>

    @Transaction
    override suspend fun deleteAllTrendingReposAndInsert(repos: List<RepoLocalEntity>) {
        deleteAll()
        insertAll(repos)
    }

    @Query("DELETE FROM repolocalentity")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(users: List<RepoLocalEntity>)
}