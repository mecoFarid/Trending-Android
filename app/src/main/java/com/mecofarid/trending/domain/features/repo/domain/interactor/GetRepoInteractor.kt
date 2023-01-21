package com.mecofarid.trending.domain.features.repo.domain.interactor

import com.mecofarid.trending.domain.common.data.Operation
import com.mecofarid.trending.domain.common.data.Query
import com.mecofarid.trending.domain.common.data.Repository
import com.mecofarid.trending.domain.features.repo.domain.model.Repo

class GetRepoInteractor(private val repoRepository: Repository<List<Repo>>) {
    suspend operator fun invoke(query: Query, operation: Operation) = repoRepository.get(query, operation)
}
