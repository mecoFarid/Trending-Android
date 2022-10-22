package com.mecofarid.trending.common.network

import com.mecofarid.trending.features.repo.data.source.remote.service.RepoService

interface NetworkComponent {
    fun repoService(): RepoService
}