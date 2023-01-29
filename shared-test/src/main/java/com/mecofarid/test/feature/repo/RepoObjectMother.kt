package com.mecofarid.test.feature.repo

import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import com.mecofarid.trending.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import com.mecofarid.trending.domain.features.trending.domain.model.Trending
import com.mecofarid.trending.randomInt
import com.mecofarid.trending.randomLong
import com.mecofarid.trending.randomString

fun anyRepoLocalEntity() =
    TrendingLocalEntity(
        randomString(),
        randomString(),
        randomLong(),
        randomString(),
        anyOwnerLocalEntity(),
        randomInt()
    )

fun anyOwnerLocalEntity() =
    TrendingLocalEntity.OwnerLocalEntity(
        randomString(),
        randomString()
    )

fun anyRepoRemoteEntity() =
    TrendingRemoteEntity(
        randomString(),
        randomString(),
        randomLong(),
        randomString(),
        anyOwnerRemoteEntity()
    )

fun anyOwnerRemoteEntity() =
    TrendingRemoteEntity.OwnerRemoteEntity(
        randomString(),
        randomString()
    )

fun anyRepo() =
    Trending(
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