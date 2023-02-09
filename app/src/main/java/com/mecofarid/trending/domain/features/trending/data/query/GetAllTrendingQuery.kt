package com.mecofarid.trending.domain.features.trending.data.query

import com.mecofarid.trending.domain.common.data.Query

data class GetAllTrendingQuery(val query: String = "language=+sort:stars"): Query
