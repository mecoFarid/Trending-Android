package com.mecofarid.trending.di.db

import com.mecofarid.trending.features.repo.data.source.local.dao.RepoLocalEntityDao

interface DbComponent{
    fun repoLocalEntityDao(): RepoLocalEntityDao
}