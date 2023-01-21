package com.mecofarid.trending.domain.di.db

import com.mecofarid.trending.domain.features.repo.data.source.local.dao.RepoLocalEntityDao

interface DbComponent{
    fun repoLocalEntityDao(): RepoLocalEntityDao
}
