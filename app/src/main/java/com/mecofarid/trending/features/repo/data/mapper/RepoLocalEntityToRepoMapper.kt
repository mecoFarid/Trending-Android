package com.mecofarid.trending.features.repo.data.mapper

import com.mecofarid.trending.common.data.Mapper
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity.OwnerLocalEntity
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity
import com.mecofarid.trending.features.repo.domain.model.Repo
import com.mecofarid.trending.features.repo.domain.model.Repo.Owner

class RepoLocalEntityToRepoMapper(
    private val ownerMapper: Mapper<OwnerLocalEntity, Owner>
): Mapper<RepoLocalEntity, Repo> {

    override fun map(input: RepoLocalEntity): Repo =
        with(input) {
            Repo(
                name,
                language,
                stargazersCount,
                description,
                ownerMapper.map(owner)
            )
        }
}

class OwnerLocalEntityToOwnerMapper: Mapper<OwnerLocalEntity, Owner> {

    override fun map(input: OwnerLocalEntity): Owner = Owner(input.login, input.avatarUrl)
}