package com.mecofarid.trending.domain.features.trending.data.source.remote.service

import com.mecofarid.trending.domain.features.trending.data.source.remote.entity.TrendingResponseRemoteEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingService {
    @GET("search/repositories")
    suspend fun searchTrending(@Query("q") query: String): TrendingResponseRemoteEntity
}
