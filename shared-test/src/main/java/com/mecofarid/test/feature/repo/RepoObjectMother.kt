package com.mecofarid.test.feature.repo

import com.mecofarid.trending.domain.features.repo.data.source.local.entity.RepoLocalEntity
import com.mecofarid.trending.domain.features.repo.data.source.remote.entity.RepoRemoteEntity
import com.mecofarid.trending.domain.features.repo.domain.model.Repo
import com.mecofarid.trending.randomInt
import com.mecofarid.trending.randomLong
import com.mecofarid.trending.randomString

fun anyRepoLocalEntity() =
    RepoLocalEntity(
        randomString(),
        randomString(),
        randomLong(),
        randomString(),
        anyOwnerLocalEntity(),
        randomInt()
    )

fun anyOwnerLocalEntity() =
    RepoLocalEntity.OwnerLocalEntity(
        randomString(),
        randomString()
    )

fun anyRepoRemoteEntity() =
    RepoRemoteEntity(
        randomString(),
        randomString(),
        randomLong(),
        randomString(),
        anyOwnerRemoteEntity()
    )

fun anyOwnerRemoteEntity() =
    RepoRemoteEntity.OwnerRemoteEntity(
        randomString(),
        randomString()
    )

fun anyRepo() =
    Repo(
        randomString(),
        randomString(),
        randomLong(),
        randomString(),
        anyOwner()
    )

fun anyOwner() =
    Repo.Owner(
        randomString(),
        randomString()
    )