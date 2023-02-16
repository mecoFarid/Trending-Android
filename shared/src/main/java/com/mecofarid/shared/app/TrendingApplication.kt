package com.mecofarid.shared.app

import android.app.Application
import com.mecofarid.shared.domain.di.db.dbModule
import com.mecofarid.shared.domain.di.network.networkModule
import com.mecofarid.shared.domain.features.trending.trendingModule
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TrendingApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TrendingApplication)
            @OptIn(ExperimentalSerializationApi::class)
            modules(
                dbModule,
                networkModule,
                trendingModule
            )
        }
    }
}
