package com.mecofarid.shared.domain.features.trending.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mecofarid.shared.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TrendingLocalEntityDao {

    @Query("SELECT * FROM trendinglocalentity")
    fun getAllTrendings(): Flowable<List<TrendingLocalEntity>>

    @Transaction
    fun deleteAllTrendingAndInsert(entityList: List<TrendingLocalEntity>) {
        deleteAll()
        insertAll(entityList)
    }

    @Query("DELETE FROM trendinglocalentity")
    fun deleteAll()

    @Insert
    fun insertAll(users: List<TrendingLocalEntity>)
}
