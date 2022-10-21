package com.mecofarid.trending.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mecofarid.trending.features.repo.data.source.local.dao.RepoLocalEntityDao
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity

@Database(entities = [RepoLocalEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): RepoLocalEntityDao
}
