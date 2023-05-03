package com.mecofarid.shared.domain.features.trending.data.query

import com.mecofarid.shared.domain.common.data.Query

class GetAllTrendingQuery(val query: String = "language=+sort:stars", val first: Int = 20): Query
