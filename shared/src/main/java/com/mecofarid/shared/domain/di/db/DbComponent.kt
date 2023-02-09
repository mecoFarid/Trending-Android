package com.mecofarid.shared.domain.di.db

import com.mecofarid.shared.domain.features.trending.data.source.local.dao.TrendingLocalEntityDao

interface DbComponent{
    fun trendingLocalEntityDao(): TrendingLocalEntityDao
}
