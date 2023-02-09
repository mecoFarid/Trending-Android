package com.mecofarid.shared.domain.features.trending.data

import com.mecofarid.shared.domain.common.data.DataException
import com.mecofarid.shared.domain.common.either.Either


typealias TrendingResult<T> = Either<DataException, List<T>>
