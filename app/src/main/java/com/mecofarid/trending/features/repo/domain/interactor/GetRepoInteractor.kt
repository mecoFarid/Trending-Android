package com.mecofarid.trending.features.repo.domain.interactor

import com.mecofarid.trending.common.data.Operation
import com.mecofarid.trending.common.data.Query
import com.mecofarid.trending.features.repo.data.RepoRepository

class GetRepoInteractor(private val repoRepository: RepoRepository) {

    suspend operator fun invoke(query: Query, operation: Operation) = repoRepository(query, operation)
}