package com.mecofarid.trending

import android.app.Application
import com.mecofarid.trending.features.repo.RepoComponent

class TrendingApplication: Application(), AppComponent {
    private val internalAppComponent by lazy { AppModule(this) }

    override fun repoComponent(): RepoComponent = internalAppComponent.repoComponent()
}

fun Application.appComponent() = this as AppComponent