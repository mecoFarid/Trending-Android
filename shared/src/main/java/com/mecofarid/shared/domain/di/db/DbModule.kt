package com.mecofarid.shared.domain.di.db

import android.content.Context
import androidx.room.Room
import com.mecofarid.shared.domain.features.trending.data.source.local.dao.TrendingLocalEntityDao
import com.mecofarid.shared.libs.db.room.TrendingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DB_NAME = "repo.db"

@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    @Singleton
    @Provides
    fun provideTrendingDatabase(
        @ApplicationContext context: Context
    ): TrendingDatabase =
        Room.databaseBuilder(
            context,
            TrendingDatabase::class.java,
            DB_NAME
        ).build()

    @Provides
    fun provideTrendingLocalEntityDao(
        trendingDatabase: TrendingDatabase
    ): TrendingLocalEntityDao = trendingDatabase.trendingLocalEntityDao()
}
