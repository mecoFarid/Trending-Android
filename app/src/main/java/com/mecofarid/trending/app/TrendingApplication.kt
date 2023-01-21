package com.mecofarid.trending.app

import android.app.Application
import com.mecofarid.trending.domain.di.AppComponent
import com.mecofarid.trending.domain.features.repo.RepoComponent

class TrendingApplication: Application(), AppComponent {
    private val internalAppComponent by lazy { AppModule(this) }

    override fun repoComponent(): RepoComponent = internalAppComponent.repoComponent()
}

fun Application.appComponent() = this as AppComponent