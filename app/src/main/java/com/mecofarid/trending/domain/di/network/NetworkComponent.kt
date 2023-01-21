package com.mecofarid.trending.domain.di.network

import com.mecofarid.trending.domain.features.repo.data.source.remote.service.RepoService

interface NetworkComponent {
    fun repoService(): RepoService
}
