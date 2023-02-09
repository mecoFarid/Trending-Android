package com.mecofarid.shared.domain.di

import com.mecofarid.shared.domain.features.trending.TrendingComponent

interface AppComponent{
    fun trendingComponent(): TrendingComponent
}
