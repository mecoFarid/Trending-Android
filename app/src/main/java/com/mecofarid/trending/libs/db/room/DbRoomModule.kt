package com.mecofarid.trending.libs.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mecofarid.trending.domain.di.db.DbComponent
import com.mecofarid.trending.domain.features.trending.data.source.local.dao.TrendingLocalEntityDao
import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity

@Database(entities = [TrendingLocalEntity::class], version = 1)
abstract class DbRoomModule : RoomDatabase(), DbComponent {
    abstract override fun trendingLocalEntityDao(): TrendingLocalEntityDao
}
