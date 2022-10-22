package com.mecofarid.trending.common.db

import com.mecofarid.trending.features.repo.data.source.local.dao.RepoLocalEntityRoomDao

interface DbComponent{
    fun repoLocalEntityDao(): RepoLocalEntityRoomDao
}