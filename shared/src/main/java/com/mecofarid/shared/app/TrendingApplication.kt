package com.mecofarid.shared.app

import android.app.Application
import com.mecofarid.shared.domain.di.AppComponent
import com.mecofarid.shared.domain.features.trending.TrendingComponent

class TrendingApplication: Application(), AppComponent {
    private val internalAppComponent by lazy { AppModule(this) }

    override fun trendingComponent(): TrendingComponent = internalAppComponent.trendingComponent()
}

fun Application.appComponent() = this as AppComponent
