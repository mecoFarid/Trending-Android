package com.mecofarid.trending.libs.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mecofarid.trending.domain.di.db.DbComponent
import com.mecofarid.trending.domain.features.repo.data.source.local.dao.RepoLocalEntityDao
import com.mecofarid.trending.domain.features.repo.data.source.local.entity.RepoLocalEntity

@Database(entities = [RepoLocalEntity::class], version = 1)
abstract class DbRoomModule : RoomDatabase(), DbComponent {
    abstract override fun repoLocalEntityDao(): RepoLocalEntityDao
}
