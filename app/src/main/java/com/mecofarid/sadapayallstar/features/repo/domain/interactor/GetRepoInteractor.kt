package com.mecofarid.sadapayallstar.features.repo.domain.interactor

import com.mecofarid.sadapayallstar.common.data.Operation
import com.mecofarid.sadapayallstar.common.data.Query
import com.mecofarid.sadapayallstar.features.repo.data.RepoRepository

class GetRepoInteractor(private val repoRepository: RepoRepository) {

    suspend operator fun invoke(query: Query, operation: Operation) = repoRepository(query, operation)
}