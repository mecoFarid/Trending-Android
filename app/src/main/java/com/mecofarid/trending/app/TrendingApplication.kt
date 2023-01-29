package com.mecofarid.trending.app

import android.app.Application
import com.mecofarid.trending.domain.di.AppComponent
import com.mecofarid.trending.domain.features.trending.TrendingComponent

class TrendingApplication: Application(), AppComponent {
    private val internalAppComponent by lazy { AppModule(this) }

    override fun trendingComponent(): TrendingComponent = internalAppComponent.trendingComponent()
}

fun Application.appComponent() = this as AppComponent
