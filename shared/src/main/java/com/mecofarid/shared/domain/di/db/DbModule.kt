package com.mecofarid.shared.domain.di.db

import androidx.room.Room
import com.mecofarid.shared.libs.db.room.TrendingDatabase
import org.koin.dsl.module

private const val DB_NAME = "trending.db"

val dbModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            TrendingDatabase::class.java,
            DB_NAME
        ).build()
    }

    factory {
        val trendingDatabase: TrendingDatabase = get()
        trendingDatabase.trendingLocalEntityDao()
    }
}
