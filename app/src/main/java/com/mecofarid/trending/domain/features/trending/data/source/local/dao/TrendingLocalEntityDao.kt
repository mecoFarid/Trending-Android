package com.mecofarid.trending.domain.features.trending.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity

@Dao
interface TrendingLocalEntityDao {

    @Query("SELECT * FROM trendinglocalentity")
    suspend fun getAllTrendings(): List<TrendingLocalEntity>

    @Transaction
    suspend fun deleteAllTrendingAndInsert(entityList: List<TrendingLocalEntity>) {
        deleteAll()
        insertAll(entityList)
    }

    @Query("DELETE FROM trendinglocalentity")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(users: List<TrendingLocalEntity>)
}
