package com.mecofarid.shared.domain.features.trending.domain.interactor

import com.mecofarid.shared.domain.common.data.DataException
import com.mecofarid.shared.domain.common.data.Operation
import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.data.repository.Repository
import com.mecofarid.shared.domain.common.either.Either
import com.mecofarid.shared.domain.features.trending.domain.model.Trending
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

class GetTrendingInteractor @Inject constructor(
    private val trendingRepository: Repository<List<Trending>, DataException>
) {

    operator fun invoke(query: Query, operation: Operation): Flow<Either<DataException, List<Trending>>> =
        trendingRepository
            .get(query, operation)
            .asFlow()
}
