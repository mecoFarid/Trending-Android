package com.mecofarid.test.feature.repo

import com.mecofarid.shared.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import com.mecofarid.shared.domain.features.trending.domain.model.Trending
import com.mecofarid.test.randomLong
import com.mecofarid.test.randomString

fun anyTrendingLocalEntity() =
    TrendingLocalEntity(
        randomString(),
        randomString(),
        randomString(),
        randomLong(),
        randomString(),
        anyOwnerLocalEntity()
    )

fun anyOwnerLocalEntity() =
    TrendingLocalEntity.OwnerLocalEntity(
        randomString(),
        randomString()
    )

fun anyTrending() =
    Trending(
        randomString(),
        randomString(),
        randomString(),
        randomLong(),
        randomString(),
        anyOwner()
    )

fun anyOwner() =
    Trending.Owner(
        randomString(),
        randomString()
    )
