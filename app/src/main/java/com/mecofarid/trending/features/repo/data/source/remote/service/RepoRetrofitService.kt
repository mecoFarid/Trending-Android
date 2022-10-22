package com.mecofarid.trending.features.repo.data.source.remote.service

import com.mecofarid.trending.features.repo.data.source.remote.entity.RepoResponseRemoteEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoRetrofitService: RepoService {
    @GET("search/repositories")
    override suspend fun searchRepos(@Query("q") query: String): RepoResponseRemoteEntity
}