package com.mecofarid.trending.features.repo

import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity
import com.mecofarid.trending.features.repo.data.source.remote.entity.RepoRemoteEntity
import com.mecofarid.trending.features.repo.domain.model.Repo
import com.mecofarid.trending.randomInt
import com.mecofarid.trending.randomLong
import com.mecofarid.trending.randomString

internal fun anyRepoLocalEntity() =
    RepoLocalEntity(
        randomString(),
        randomString(),
        randomLong(),
        randomString(),
        anyOwnerLocalEntity(),
        randomInt()
    )

internal fun anyOwnerLocalEntity() =
    RepoLocalEntity.OwnerLocalEntity(
        randomString(),
        randomString()
    )

internal fun anyRepoRemoteEntity() =
    RepoRemoteEntity(
        randomString(),
        randomString(),
        randomLong(),
        randomString(),
        anyOwnerRemoteEntity()
    )

internal fun anyOwnerRemoteEntity() =
    RepoRemoteEntity.OwnerRemoteEntity(
        randomString(),
        randomString()
    )

internal fun anyRepo() =
    Repo(
        randomString(),
        randomString(),
        randomLong(),
        randomString(),
        anyOwner()
    )

internal fun anyOwner() =
    Repo.Owner(
        randomString(),
        randomString()
    )