package com.mecofarid.trending.common.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mecofarid.trending.common.db.DbComponent
import com.mecofarid.trending.features.repo.data.source.local.dao.RepoLocalEntityRoomDao
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity

@Database(entities = [RepoLocalEntity::class], version = 1)
abstract class DbRoomModule : RoomDatabase(), DbComponent {
    abstract override fun repoLocalEntityDao(): RepoLocalEntityRoomDao
}
