package com.mecofarid.trending.domain.features.trending.data

import com.mecofarid.trending.domain.common.either.Either
import com.mecofarid.trending.domain.common.data.DataException

typealias TrendingResult<T> = Either<DataException, List<T>>
