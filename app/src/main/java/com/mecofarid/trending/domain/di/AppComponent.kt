package com.mecofarid.trending.domain.di

import com.mecofarid.trending.domain.features.trending.TrendingComponent

interface AppComponent{
    fun trendingComponent(): TrendingComponent
}
