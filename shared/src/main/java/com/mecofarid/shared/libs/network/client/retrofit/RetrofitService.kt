package com.mecofarid.shared.libs.network.client.retrofit

import com.mecofarid.shared.domain.features.trending.data.source.remote.entity.TrendingResponseRemoteEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("search/repositories")
    suspend fun searchTrending(@Query("q") query: String): TrendingResponseRemoteEntity
}
