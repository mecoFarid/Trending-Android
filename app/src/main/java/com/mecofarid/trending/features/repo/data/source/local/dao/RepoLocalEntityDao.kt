package com.mecofarid.trending.features.repo.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity

@Dao
interface RepoLocalEntityDao {

    @Query("SELECT * FROM repolocalentity")
    fun getAllTrendingRepos(): List<RepoLocalEntity>

    @Transaction
    fun deleteAllTrendingReposAndInsert(repos: List<RepoLocalEntity>) {
        deleteAll()
        insertAll(repos)
    }

    @Query("DELETE FROM repolocalentity")
    fun deleteAll()

    @Insert
    fun insertAll(users: List<RepoLocalEntity>)
}