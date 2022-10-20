package com.mecofarid.sadapayallstar.features.repo.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mecofarid.sadapayallstar.features.repo.data.source.local.entity.RepoLocalEntity

@Dao
interface RepoLocalEntityDao {

    @Query("SELECT * FROM repolocalentity")
    fun getAllStarredRepos(): List<RepoLocalEntity>

    @Transaction
    fun deleteAllStarredReposAndInsert(repos: List<RepoLocalEntity>) {
        deleteAll()
        insertAll(repos)
    }

    @Query("DELETE FROM repolocalentity")
    fun deleteAll()

    @Insert
    fun insertAll(users: List<RepoLocalEntity>)
}