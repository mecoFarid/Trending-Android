package com.mecofarid.trending.domain.features.repo.data.source.remote.service

import com.mecofarid.trending.domain.features.repo.data.source.remote.entity.RepoResponseRemoteEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoService {
    @GET("search/repositories")
    suspend fun searchRepos(@Query("q") query: String): RepoResponseRemoteEntity
}
