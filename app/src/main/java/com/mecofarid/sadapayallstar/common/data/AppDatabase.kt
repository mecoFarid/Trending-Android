package com.mecofarid.sadapayallstar.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mecofarid.sadapayallstar.features.repo.data.source.local.dao.RepoLocalEntityDao
import com.mecofarid.sadapayallstar.features.repo.data.source.local.entity.RepoLocalEntity

@Database(entities = [RepoLocalEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): RepoLocalEntityDao
}
