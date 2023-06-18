package com.mecofarid.shared.libs.network.client.retrofit

import com.mecofarid.shared.domain.features.trending.data.source.remote.entity.TrendingResponseRemoteEntity
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("search/repositories")
    fun searchTrending(@Query("q") query: String): Single<TrendingResponseRemoteEntity>
}
