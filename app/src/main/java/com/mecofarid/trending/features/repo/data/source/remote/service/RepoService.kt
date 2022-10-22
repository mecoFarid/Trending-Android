package com.mecofarid.trending.features.repo.data.source.remote.service

import com.mecofarid.trending.features.repo.data.source.remote.entity.RepoResponseRemoteEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoService {
    suspend fun searchRepos(query: String): RepoResponseRemoteEntity
}