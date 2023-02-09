package com.mecofarid.shared.domain.features.trending.data.query

import com.mecofarid.shared.domain.common.data.Query

data class GetAllTrendingQuery(val query: String = "language=+sort:stars"): Query
