package com.mecofarid.sadapayallstar.features.repo.data.mapper

import com.mecofarid.sadapayallstar.common.data.Mapper
import com.mecofarid.sadapayallstar.features.repo.data.source.local.entity.OwnerLocalEntity
import com.mecofarid.sadapayallstar.features.repo.data.source.local.entity.RepoLocalEntity
import com.mecofarid.sadapayallstar.features.repo.domain.model.Repo
import com.mecofarid.sadapayallstar.features.repo.domain.model.Repo.Owner

class RepoLocalEntityToRepoMapper(
    private val ownerMapper: Mapper<OwnerLocalEntity, Owner>
): Mapper<List<RepoLocalEntity>, List<Repo>> {

    override fun map(input: List<RepoLocalEntity>): List<Repo> =
        input.map {
            Repo(
                it.name,
                it.language,
                it.stargazersCount,
                it.description,
                ownerMapper.map(it.owner)
            )
        }
}

class OwnerLocalEntityToOwnerMapper: Mapper<OwnerLocalEntity, Owner> {

    override fun map(input: OwnerLocalEntity): Owner = Owner(input.login, input.avatarUrl)
}